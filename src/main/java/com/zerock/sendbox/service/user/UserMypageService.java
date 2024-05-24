package com.zerock.sendbox.service.user;

import com.zerock.sendbox.entity.Orders;
import com.zerock.sendbox.entity.Room;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.repository.OrdersRepository;
import com.zerock.sendbox.repository.RoomRepository;
import com.zerock.sendbox.repository.UserMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserMypageService {
    @Autowired
    UserMemberRepository userMemberRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    RoomRepository roomRepository;


    //개인 정보 수정폼
    public UserMember findByUserId(String userId) {
        return userMemberRepository.findByUserId(userId);
    }

    //개인 정보 수정
    public UserMember updateInfo(UserMember userMember) {
        return userMemberRepository.save(userMember);
    }

    //개인정보 조회
    public UserMember findByUserNo(Integer userNo) {
        return userMemberRepository.findByUserNo(userNo);
    }

    //개인 정보 탈퇴
    @Transactional
    public Integer deleteInfo(Integer userNo) {
       return userMemberRepository.deleteInfo(userNo);
    }

    //유저의 예약 내역 리스트 조회
    public List<Orders> findAllReservation(Integer userNo) {
        return ordersRepository.findAllReservation(userNo);
    }

    //장바구니 리스트 조회
    public List<Orders> findAllCartList(Integer userNo) {
        return ordersRepository.findAllCartList(userNo);
    }

    //장바구니 옵션 수정
    public Orders save(Orders orders) {
        return ordersRepository.save(orders);
    }

    //orderNo에 맞는 장바구니 품목 한 개 가져오기
    public Orders findByOrderNo(Integer orderNo) {
        return ordersRepository.findByOrderNo(orderNo);
    }

    //장바구니 단건 삭제
    @Transactional
    public Integer deleteCart(Integer orderNo) {
        return ordersRepository.deleteByOrderNo(orderNo);
    }


    // storeNo를 통해 한 업체가 갖고 있는 room 리스트를 가져옴
    public List<Room> findAllRoomList(Integer storeNo) {
        return roomRepository.findByStoreNo(storeNo);
    }

    //장바구니 전체 삭제
    @Transactional
    public Integer deleteAllCart(Integer userNo) {
        return ordersRepository.deleteAllCart(userNo);
    }

    //roomNo로 룸정보 가져오기
    public Room findByRoomNo(Integer roomNo) {
        return roomRepository.findByRoomNo(roomNo);
    }

    //장바구니 담기
    @Transactional
    public Orders saveOrders(Orders orders) {
        return ordersRepository.save(orders);
    }

    //예약 번호 클릭으로 예약 내역 조회
    public Orders findAllByOrderNo(Integer orderNo) {
        return ordersRepository.findPaymentInfo(orderNo);
    }

}
