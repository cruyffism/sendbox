package com.zerock.sendbox.service.user;

import com.zerock.sendbox.dto.user.UserLoginDTO;
import com.zerock.sendbox.entity.UserMember;

public interface UserLoginService {

    UserMember authenticate(UserLoginDTO userLoginDTO);

    void sendPasswordResetMail(String mail);

    UserMember findByMail(String mail);


    boolean sendMail(String mail, String subject, String text);

    UserMember findByUserIdAndMail(String userId, String mail);

    UserMember findByNameAndMail(String name, String mail);
}