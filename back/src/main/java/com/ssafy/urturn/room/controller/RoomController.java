package com.ssafy.urturn.room.controller;

import com.ssafy.urturn.room.service.RoomService;
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
    public ResponseEntity<String> checkRoomEntry(@PathVariable String entryCode){
        String roomId= roomService.canEnterRoom(entryCode);
        if(roomId!=null) return ResponseEntity.ok(roomId);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("입장 불가");
    }
}
