package com.zerock.sendbox.service.admin;

import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.entity.OwnerMember;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.repository.AdminMemberRepository;
import com.zerock.sendbox.repository.OwnerMemberRepository;
import com.zerock.sendbox.repository.UserMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminManagementService {
    @Autowired
    UserMemberRepository userMemberRepository;

    @Autowired
    OwnerMemberRepository ownerMemberRepository;

    @Autowired
    AdminMemberRepository adminMemberRepository;


    //admin의 모든 유저 리스트 조회
    public List<UserMember> findAllByDeleteYn(String deleteYn) {
        return userMemberRepository.findAllByDeleteYn(deleteYn);
    }

    //admin의 모든 오너 리스트 조회
    public List<OwnerMember> findByDeleteYn(String deleteYn) {
        return ownerMemberRepository.findAllByDeleteYn(deleteYn);
    }

    //모든 매니저 리스트 조회
    public List<AdminMember> findManager(String deleteYn) {
        return adminMemberRepository.findAllByDeleteYn(deleteYn);
    }

    //매니저 승인
    @Transactional
    public Integer saveGrant(Integer adminNo) {
        return adminMemberRepository.saveGrant(adminNo);
    }

    //admin의 유저 단건 삭제
    @Transactional
    public Integer findByDeleteYn(Integer userNo) {
        return userMemberRepository.findByDeleteYn(userNo);
    }

    //admin의 오너 단건 삭제
    @Transactional
    public Integer deleteOwner(Integer ownerNo) {
        return ownerMemberRepository.deleteInfo(ownerNo);
    }

    //admin의 오너 회원가입 승인
    @Transactional
    public Integer saveApproval(Integer ownerNo) {
        return ownerMemberRepository.saveApproval(ownerNo);
    }
}
