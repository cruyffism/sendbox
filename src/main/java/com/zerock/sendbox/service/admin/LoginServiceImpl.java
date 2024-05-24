package com.zerock.sendbox.service.admin;

import com.zerock.sendbox.dto.admin.LoginDTO;
import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.repository.AdminMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final AdminMemberRepository adminMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public AdminMember authenticate(LoginDTO loginDTO) {
        String adminId = loginDTO.getAdminId();
        String password = loginDTO.getPassword();

        AdminMember adminMember = adminMemberRepository.findByAdminId(adminId);

        if (adminMember != null && passwordEncoder.matches(password, adminMember.getPassword())) {
            // 패스워드가 일치하면 사용자 반환
            return adminMember;
        }
        // 인증 실패
        return null;
    }

    @Override
    public void sendPasswordResetMail(String mail) {
        String temporaryPassword = generateTemporaryPassword();
        // 임시 비밀번호를 사용자의 이메일로 전송
        boolean mailSent = sendMail(mail, "비밀번호 재설정 안내", "임시 비밀번호: " + temporaryPassword);

        // 이메일 전송 결과에 따라 처리
        if (mailSent) {
            AdminMember adminMember = adminMemberRepository.findByMail(mail);
            if (adminMember != null) {
                // 임시 비밀번호를 암호화하여 저장
                adminMember.setPassword(passwordEncoder.encode(temporaryPassword));
                adminMemberRepository.save(adminMember);
            }
        } else {
            // 이메일 전송 실패 처리
            // 예: 로깅 또는 사용자에게 메시지 전송
        }
    }

    @Override
    public AdminMember findByMail(String mail) {
        return adminMemberRepository.findByMail(mail);
    }

    @Override
    public AdminMember findByNameAndMail(String name, String mail) {
        return adminMemberRepository.findByNameAndMail(name,mail);
    }

    @Override
    public AdminMember findByAdminIdAndMail(String adminId, String mail) {
        return adminMemberRepository.findByAdminIdAndMail(adminId, mail);
    }

    private String generateTemporaryPassword() {
        // 보다 안전한 임시 비밀번호 생성 로직
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        // 무작위 바이트 배열을 문자열로 변환하여 반환
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public boolean sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            return true; // 이메일 전송 성공
        } catch (MailException e) {
            // 이메일 전송 실패
            return false;
        }
    }
}

