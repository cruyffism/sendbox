package com.zerock.sendbox.service.room;

import com.zerock.sendbox.entity.Room;
import com.zerock.sendbox.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room getRoomByRoomNoAndStoreNo(Integer roomNo, Integer storeNo) {
        return roomRepository.findByRoomNoAndStore_StoreNo(roomNo, storeNo);
    }
}




