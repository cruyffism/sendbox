package com.zerock.sendbox.service.user;

import com.zerock.sendbox.dto.user.UserSignUpDTO;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.repository.UserMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignUpServiceImpl implements UserSignUpService{
    private final UserMemberRepository userMemberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Integer join(UserSignUpDTO userSignUpDTO, MemberRole memberRole) {

        UserMember userMember = UserMember.builder()
                .userNo(userSignUpDTO.getUserNo())
                .memberRole(memberRole)
                .userId(userSignUpDTO.getUserId())
                .password(passwordEncoder.encode(userSignUpDTO.getPassword()))
                .name(userSignUpDTO.getName())
                .gender(userSignUpDTO.getGender())
                .mail(userSignUpDTO.getMail())
                .phone(userSignUpDTO.getPhone())
                .deleteYn("N")
                .build();
        return userMemberRepository.save(userMember).getUserNo();
    }

    @Override
    public UserSignUpDTO entityToDTO(UserMember userMember) {
        UserSignUpDTO userSignUpDTO = UserSignUpDTO.builder()
                .userNo(userMember.getUserNo())
                .userId(userMember.getUserId())
                .password(userMember.getPassword())
                .name(userMember.getName())
                .gender(userMember.getGender())
                .mail(userMember.getMail())
                .phone(userMember.getPhone())
                .deleteYn("N")
                .regDate(userMember.getRegDate())
                .modDate(userMember.getModDate())
                .build();
        return userSignUpDTO;
    }

    @Override
    public boolean isIdDuplicated(String userId) {
        UserMember existingMember = userMemberRepository.findByUserId(userId);

        return existingMember != null;
    }

}


