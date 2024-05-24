package com.zerock.sendbox.controller.room;

import com.zerock.sendbox.entity.Room;
import com.zerock.sendbox.service.room.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/store/{storeNo}/room/{roomNo}")
    public String getRoomDetail(Model model, @PathVariable Integer storeNo, @PathVariable Integer roomNo) {
        Room room = roomService.getRoomByRoomNoAndStoreNo(roomNo, storeNo);

        if (room != null) {
            model.addAttribute("room", room);
            model.addAttribute("store", room.getStore());
            return "room_detail";
        } else {
            return "error"; // 에러 페이지로 리다이렉트 또는 에러 메시지를 표시할 수 있습니다.
        }
    }
}

