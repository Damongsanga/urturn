package com.ssafy.urturn.solving.controller;

import com.ssafy.urturn.solving.cache.cacheDatas;
import com.ssafy.urturn.solving.service.RoomService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/room")
@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    @GetMapping("/enter/{entryCode}")
    public ResponseEntity<?> checkRoomEntry(@PathVariable String entryCode){
        String roomId= roomService.canEnterRoom(entryCode);
        // 방 ID 반환.
        if(roomId!=null) return ResponseEntity.ok(roomId);
        // 서비스단에서 처리했으니 추 후 바꿔야함.
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("입장 불가");
    }
}
