package com.zerock.sendbox.service.user;

import com.zerock.sendbox.dto.user.UserLoginDTO;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.repository.UserMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService{

    private final UserMemberRepository userMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public UserMember authenticate(UserLoginDTO userLoginDTO) {
        String userId = userLoginDTO.getUserId();
        String password = userLoginDTO.getPassword();

        UserMember userMember = userMemberRepository.findByUserId(userId);

        if (userMember != null && passwordEncoder.matches(password, userMember.getPassword())) {
            return userMember;
        }
        return null;
    }

    @Override
    public void sendPasswordResetMail(String mail) {
        String temporaryPassword = generateTemporaryPassword();

        boolean mailSent = sendMail(mail, "비밀번호 재설정 안내", "임시 비밀번호: " + temporaryPassword);

        if(mailSent) {
            UserMember userMember = userMemberRepository.findByMail(mail);
            if (userMember != null) {
                userMember.setPassword(passwordEncoder.encode(temporaryPassword));
                userMemberRepository.save(userMember);
            }
        } else {
            // 이메일 전송 실패 처리
            // 예: 로깅 또는 사용자에게 메시지 전송
        }

    }

    @Override
    public UserMember findByMail(String mail) {
        return userMemberRepository.findByMail(mail);
    }

    @Override
    public UserMember findByNameAndMail(String name, String mail) {
        return userMemberRepository.findByNameAndMail(name, mail);
    }

    @Override
    public UserMember findByUserIdAndMail(String userId, String mail) {
        return userMemberRepository.findByUserIdAndMail(userId, mail);
    }

    private String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public boolean sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            // 이메일 전송 실패
            return false;
        }
    }

}


