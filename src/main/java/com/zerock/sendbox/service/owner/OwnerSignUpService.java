package com.zerock.sendbox.service.owner;


import com.zerock.sendbox.dto.owner.OwnerSignUpDTO;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.entity.OwnerMember;


public interface OwnerSignUpService {

    Integer join(OwnerSignUpDTO ownerSignUpDTO, MemberRole memberRole);

    OwnerSignUpDTO entityToDTO(OwnerMember ownerMember);

    boolean isIdDuplicated(String ownerId);
}
