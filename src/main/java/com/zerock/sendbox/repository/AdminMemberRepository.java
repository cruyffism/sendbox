package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.AdminMember;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMemberRepository extends JpaRepository<AdminMember, Integer> {

    // 개인 정보 수정폼
    AdminMember findByAdminId(String adminId);
    AdminMember findByAdminIdAndMail(String adminId, String mail);


    // 개인 정보 조회
    AdminMember findByAdminNo(Integer adminNo);

    //개인 정보 탈퇴
    @Modifying //@쿼리 어노테이션(jpql쿼리, native쿼리)을 통해서 작성된 insert, update, delete 쿼리에서 사용됨!
    @Query("update AdminMember set deleteYn = 'y' where adminNo =:adminNo")
    Integer deleteInfo(@Param("adminNo")Integer adminNo);


    AdminMember findByMail(String mail);

    //모든 매니저 리스트 조회
    List<AdminMember> findAllByDeleteYn(String deleteYn);

    //매니저 승인
    @Modifying //@쿼리 어노테이션(jpql쿼리, native쿼리)을 통해서 작성된 insert, update, delete 쿼리에서 사용됨!
    @Query("update AdminMember set approval = 1 where adminNo =:adminNo")
    Integer saveGrant(@Param("adminNo")Integer adminNo);


    AdminMember findByNameAndMail(String name, String mail);
}
