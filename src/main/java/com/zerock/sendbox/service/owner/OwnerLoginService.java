package com.zerock.sendbox.service.owner;

import com.zerock.sendbox.dto.owner.OwnerLoginDTO;
import com.zerock.sendbox.entity.OwnerMember;

public interface OwnerLoginService {

    OwnerMember authenticate(OwnerLoginDTO ownerLoginDTO);

    void sendPasswordResetMail(String mail);


    OwnerMember findByMail(String mail);

    OwnerMember findByOwnerIdAndMail(String ownerId, String mail);

    boolean sendMail(String mail, String subject, String text);

    OwnerMember findByNameAndMail(String name, String mail);
}
