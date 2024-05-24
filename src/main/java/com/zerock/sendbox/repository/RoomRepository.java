package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.Room;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    // storeNo를 통해 한 업체가 갖고 있는 room 리스트를 가져옴
    @Query("select r from Room r where r.store.storeNo =:storeNo ")
    List<Room> findByStoreNo(@Param("storeNo") Integer storeNo);

    //roomNo로 룸정보 가져오기
    Room findByRoomNo(Integer roomNo);

    Room findByRoomNoAndStore_StoreNo(Integer roomNo, Integer storeNo);

    //장바구니 담기위해 가격 조회
    @Query("select r.price from Room r where r.store.storeNo =:storeNo and r.size =:roomSize")
    int findPriceByStoreAndRoomSize(@Param("storeNo") int storeNo, @Param("roomSize") String roomSize);

}
