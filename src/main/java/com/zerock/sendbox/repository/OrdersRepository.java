package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.Orders;
import com.zerock.sendbox.entity.Store;
import com.zerock.sendbox.entity.UserMember;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    //유저의 예약 내역 리스트 조회
    @Query("select o,r,s from Orders o inner join Room r on o.room.roomNo = r.roomNo " +
            "inner join Store s on r.store.storeNo = s.storeNo " +
            "where o.reservationStatus != '예약대기' and o.userMember.userNo =:userNo ORDER BY o.startDate DESC")
    List<Orders> findAllReservation(@Param("userNo") Integer userNo);

    //장바구니 리스트 조회
    @Query("select o,r,s from Orders o inner join Room r on o.room.roomNo = r.roomNo " +
            "inner join Store s on r.store.storeNo = s.storeNo where o.reservationStatus = '예약대기' and o.userMember.userNo =:userNo ORDER BY o.startDate DESC")
    List<Orders> findAllCartList(@Param("userNo") Integer userNo);

    //orderNo에 맞는 장바구니 품목 한 개 가져오기
    Orders findByOrderNo(Integer orderNo);

    //장바구니 단건 삭제
    Integer deleteByOrderNo(Integer orderNo);

    //장바구니 전체 삭제
    @Modifying // delete 쿼리 사용 시 필요함 그래야 인식 가능?
    @Query("delete from Orders o where o.reservationStatus = '예약대기' and o.userMember.userNo =:userNo")
    Integer deleteAllCart(@Param("userNo") Integer userNo);

    //사업자의 예약 내역 리스트 조회
    @Query("select o,r,s from Orders o inner join Room r on o.room.roomNo = r.roomNo " +
            "inner join Store s on r.store.storeNo = s.storeNo where o.reservationStatus != '예약대기' and s.ownerMember.ownerNo =:ownerNo ORDER BY o.startDate DESC")
    List<Orders> findAllUserReservation(@Param("ownerNo") Integer ownerNo);


    // orders에 paymentId 저장
    @Modifying
    @Query("update Orders o set o.payment.paymentNo =:paymentNo where o.orderNo =:orderNo")
    Integer payment(@Param("orderNo") Integer orderNo,@Param("paymentNo") Integer paymentNo);


    @Modifying
    @Query("update Orders o set o.reservationStatus ='결제완료' where o.payment.paymentNo =:paymentNo")
    Integer updateOrder(@Param("paymentNo") Integer paymentNo);

    @Query("select o from Orders o where o.payment.paymentNo =:paymentNo")
    Orders findOrder(@Param("paymentNo") Integer paymentNo);

    //결제 취소 api
    @Modifying
    @Query("update Orders o set o.reservationStatus ='결제취소' where o.orderNo =:orderNo")
    Integer paymentCancel(@Param("orderNo") Integer orderNo);

    //tid 및 가격 찾기 위한 메서드
    @Query("select  o, p from Orders o inner join Payment p on o.payment.paymentNo = p.paymentNo where o.orderNo =:orderNo ")
    Orders findPaymentInfo(@Param("orderNo") Integer orderNo);
}
