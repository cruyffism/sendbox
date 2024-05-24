package com.zerock.sendbox.service.admin;

import com.zerock.sendbox.dto.admin.SignUpDTO;
import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.repository.AdminMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService{

    private final AdminMemberRepository adminMemberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Integer join(SignUpDTO signUpDTO, MemberRole memberRole) {


        AdminMember adminMember = AdminMember.builder()
                .adminNo(signUpDTO.getAdminNo())
                .memberRole(memberRole)
                .approval(signUpDTO.getApproval())
                .adminId(signUpDTO.getAdminId())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .name(signUpDTO.getName())
                .gender(signUpDTO.getGender())
                .mail(signUpDTO.getMail())
                .phone(signUpDTO.getPhone())
                .part(signUpDTO.getPart())
                .deleteYn("N")
                .build();
        return adminMemberRepository.save(adminMember).getAdminNo();

    }

    @Override
    public SignUpDTO entityToDTO(AdminMember adminMember) {
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .adminNo(adminMember.getAdminNo())
                .approval(1)
                .adminId(adminMember.getAdminId())
                .password(adminMember.getPassword())
                .name(adminMember.getName())
                .gender(adminMember.getGender())
                .mail(adminMember.getMail())
                .phone(adminMember.getPhone())
                .part(adminMember.getPart())
                .deleteYn("N")
                .regDate(adminMember.getRegDate())
                .modDate(adminMember.getModDate())
                .build();
        return signUpDTO;
    }

    @Override
    public boolean isIdDuplicated(String adminId) {
        // adminId를 이용하여 데이터베이스에서 회원을 조회합니다.
        AdminMember existingMember = adminMemberRepository.findByAdminId(adminId);

        // 조회 결과가 null이면 중복되지 않은 아이디입니다.
        return existingMember != null;
    }
}
