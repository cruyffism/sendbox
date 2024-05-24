package com.zerock.sendbox.service.owner;

import com.zerock.sendbox.dto.owner.OwnerLoginDTO;
import com.zerock.sendbox.entity.OwnerMember;
import com.zerock.sendbox.repository.OwnerMemberRepository;
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
public class OwnerLoginServiceImpl implements OwnerLoginService{

    private final OwnerMemberRepository ownerMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public OwnerMember authenticate(OwnerLoginDTO ownerLoginDTO) {
        String ownerId = ownerLoginDTO.getOwnerId();
        String password = ownerLoginDTO.getPassword();

        OwnerMember ownerMember = ownerMemberRepository.findByOwnerId(ownerId);

        if (ownerMember != null && passwordEncoder.matches(password, ownerMember.getPassword())) {
            return ownerMember;
        }
        return null;
    }

    @Override
    public void sendPasswordResetMail(String mail) {
        String temporaryPassword = generateTemporaryPassword();

        boolean mailSent = sendMail(mail, "비밀번호 재설정 안내", "임시 비밀번호: " + temporaryPassword);

        if (mailSent) {
            OwnerMember ownerMember = ownerMemberRepository.findByMail(mail);
            if (ownerMember != null) {
                // 임시 비밀번호를 암호화하여 저장
                ownerMember.setPassword(passwordEncoder.encode(temporaryPassword));
                ownerMemberRepository.save(ownerMember);
            }
        } else {
            // 이메일 전송 실패 처리
            // 예: 로깅 또는 사용자에게 메시지 전송
        }
    }

    @Override
    public OwnerMember findByMail(String mail) {

        return ownerMemberRepository.findByMail(mail);
    }

    @Override
    public OwnerMember findByNameAndMail(String name, String mail) {

        return ownerMemberRepository.findByNameAndMail(name, mail);
    }

    @Override
    public OwnerMember findByOwnerIdAndMail(String ownerId, String mail) {
        return ownerMemberRepository.findByOwnerIdAndMail(ownerId, mail);
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
