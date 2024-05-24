package com.zerock.sendbox.service.admin;

import com.zerock.sendbox.dto.admin.SignUpDTO;
import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.entity.MemberRole;

public interface SignUpService {
    Integer join(SignUpDTO signUpDTO, MemberRole memberRole);


    SignUpDTO entityToDTO(AdminMember adminMember);


    boolean isIdDuplicated(String adminId);


}
