package com.ssafy.urturn.test;

import com.ssafy.urturn.solving.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/test")
@RestController
@RequiredArgsConstructor
public class testController {
    private final RoomService roomService;
    @GetMapping("/test")
    public ResponseEntity<?> checkRoomEntry(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("성공");
    }
}
