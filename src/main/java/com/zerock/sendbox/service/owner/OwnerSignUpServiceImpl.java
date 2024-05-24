package com.zerock.sendbox.service.owner;

import com.zerock.sendbox.dto.owner.OwnerSignUpDTO;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.entity.OwnerMember;
import com.zerock.sendbox.repository.OwnerMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerSignUpServiceImpl implements OwnerSignUpService {

    private final OwnerMemberRepository ownerMemberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Integer join(OwnerSignUpDTO ownerSignUpDTO, MemberRole memberRole) {

        OwnerMember ownerMember = OwnerMember.builder()
                .ownerNo(ownerSignUpDTO.getOwnerNo())
                .memberRole(memberRole)
                .ownerId(ownerSignUpDTO.getOwnerId())
                .password(passwordEncoder.encode(ownerSignUpDTO.getPassword()))
                .name(ownerSignUpDTO.getName())
                .gender(ownerSignUpDTO.getGender())
                .mail(ownerSignUpDTO.getMail())
                .phone(ownerSignUpDTO.getPhone())
                .approvalYn("N")
                .deleteYn("Y")
                .build();
        return ownerMemberRepository.save(ownerMember).getOwnerNo();
    }


    @Override
    public OwnerSignUpDTO entityToDTO(OwnerMember ownerMember) {
        OwnerSignUpDTO ownerSignUpDTO = OwnerSignUpDTO.builder()
                .ownerNo(ownerMember.getOwnerNo())
                .ownerId(ownerMember.getOwnerId())
                .password(ownerMember.getPassword())
                .name(ownerMember.getName())
                .gender(ownerMember.getGender())
                .mail(ownerMember.getMail())
                .phone(ownerMember.getPhone())
                .deleteYn("N")
                .regDate(ownerMember.getRegDate())
                .modDate(ownerMember.getModDate())
                .build();
        return ownerSignUpDTO;
    }

    @Override
    public boolean isIdDuplicated(String ownerId) {
        OwnerMember existingMember = ownerMemberRepository.findByOwnerId(ownerId);

        return existingMember != null;
    }
}
