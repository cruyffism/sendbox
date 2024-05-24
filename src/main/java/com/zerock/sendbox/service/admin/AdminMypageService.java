package com.zerock.sendbox.service.admin;

import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.repository.AdminMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMypageService {
    @Autowired
    AdminMemberRepository adminMemberRepository;

    // 개인 정보 수정폼
    public AdminMember findByAdminId(String adminId) {
        return adminMemberRepository.findByAdminId(adminId);
    }

    // 개인 정보 수정
    public AdminMember updateInfo(AdminMember adminMember) {
        return adminMemberRepository.save(adminMember);
    }

    //개인 정보 조회
    public AdminMember findByAdminNo(Integer adminNo) {
        return adminMemberRepository.findByAdminNo(adminNo);
    }

    //개인 정보 탈퇴
    @Transactional
    public Integer deleteInfo(Integer adminNo) {
        return adminMemberRepository.deleteInfo(adminNo);
    }
}
