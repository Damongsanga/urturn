package com.ssafy.urturn.global.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
@Slf4j
public class CheckController {
    @GetMapping("")
    public String checkServerStatus(){
        return "check";
    }
}
