package com.zerock.sendbox.service.user;

import com.zerock.sendbox.dto.user.UserSignUpDTO;
import com.zerock.sendbox.entity.MemberRole;
import com.zerock.sendbox.entity.UserMember;

public interface UserSignUpService {

    Integer join(UserSignUpDTO userSignUpDTO, MemberRole memberRole);

    UserSignUpDTO entityToDTO(UserMember userMember);

    boolean isIdDuplicated(String userId);
}
