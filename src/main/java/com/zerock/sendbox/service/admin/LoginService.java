package com.zerock.sendbox.service.admin;

import com.zerock.sendbox.dto.admin.LoginDTO;
import com.zerock.sendbox.dto.admin.SignUpDTO;
import com.zerock.sendbox.entity.AdminMember;

public interface LoginService {
    AdminMember authenticate(LoginDTO loginDTO);

    void sendPasswordResetMail(String mail);

    AdminMember findByMail(String mail);

    AdminMember findByAdminIdAndMail(String adminId, String mail);

    boolean sendMail(String to, String subject, String body);

    AdminMember findByNameAndMail(String name, String mail);
}
