package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.UserMember;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMemberRepository extends JpaRepository<UserMember, Integer> {

    //개인 정보 수정폼
    UserMember findByUserId(String userId);

    //개인정보 조회
    UserMember findByUserNo(Integer userNo);

    //개인 정보 탈퇴 >> Integer(리턴 타입)에 값(1 혹은 0)이 담겨서 다시 반환한다.
    @Modifying // @쿼리 어노테이션(jpql쿼리, native쿼리)을 통해서 작성된 insert, update, delete 쿼리에서 사용됨!
    @Query("update UserMember set deleteYn = 'Y' where userNo =:userNo")
    Integer deleteInfo(@Param("userNo") Integer userNo); // @Param을 통해 ownerNo를 보내서 쿼리 실행 후 리턴!

    //admin의 모든 유저 리스트 조회
    List<UserMember> findAllByDeleteYn(String deleteYn);

    //admin의 유저 단건 삭제
    @Modifying // @쿼리 어노테이션(jpql쿼리, native쿼리)을 통해서 작성된 insert, update, delete 쿼리에서 사용됨!
    @Query("update UserMember set deleteYn = 'Y' where userNo =:userNo")
    Integer findByDeleteYn(Integer userNo);


    UserMember findByMail(String mail);

    UserMember findByUserIdAndMail(String userId, String mail);

    UserMember findByNameAndMail(String name, String mail);
}
