package com.ssafy.urturn.member.controller;


import com.ssafy.urturn.member.dto.MemberDetailResponse;
import com.ssafy.urturn.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<MemberDetailResponse> get(){
        return ResponseEntity.ok(memberService.getCurrentMember());
    }

    @PatchMapping("")
    public ResponseEntity<Void> update( @RequestParam String repository){
        memberService.updateGithubRepository(repository);
        return ResponseEntity.ok().build();
    }

}
