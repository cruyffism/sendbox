package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.Store;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    //매장 정보 수정폼 조회
    @Query("select s from Store s  where s.ownerMember.ownerNo =:ownerNo")
    Store findByInfoOwnerNo(@Param("ownerNo")Integer ownerNo);

    //매장 정보 수정 & 상세페이지
    Store findByStoreNo(Integer storeNo);
//    Store findByStoreName(String storeName);

    //검색기능
    @Query("select s from Store s where s.storeName like concat('%', :storeName ,'%') and s.ownerMember.deleteYn = 'N' and s.ownerMember.approvalYn ='Y'")
    List<Store> findAllByStoreNameContaining(@Param("storeName") String storeName);


    //사업자의 예약 내역 리스트 조회
    @Query("select s,r,o,u from Store s inner join Room r on s.storeNo = r.store.storeNo " +
            "inner join Orders o on r.roomNo = o.room.roomNo " +
            "inner join UserMember u on o.userMember.userNo = u.userNo where o.reservationStatus != '예약대기' and r.store.ownerMember.ownerNo =:ownerNo")
    List<Store> findAllUserReservation(@Param("ownerNo") Integer ownerNo);

    //상세페이지
//    Store findByStoreNo(Integer storeNo);

    // admin의 모든 업체 리스트 조회
    @Query("select s,o from Store s inner join OwnerMember o on s.ownerMember.ownerNo = o.ownerNo where o.deleteYn = 'N'")
    List<Store> findStore();

    //퀵메뉴
    @Query("select s,o from Store s inner join OwnerMember o on s.ownerMember.ownerNo = o.ownerNo where o.deleteYn = 'N' and o.approvalYn = 'Y' and s.region =:region")
    List<Store> findByRegion(@Param("region")String region);

}
