package com.ssafy.urturn.github;

import com.ssafy.urturn.history.dto.HistoryRetroDto;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.member.entity.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class GithubUploadUtil {

    static final String GITHUB_API_URL = "https://api.github.com/repos/";

    public String makeContent(HistoryRetroDto dto){
        StringBuilder sb = new StringBuilder();

        // 요약 부분
        sb.append("# 요약\n")
            .append("방장 : ").append(dto.getManagerNickname()).append("\n\n")
            .append("페어 : ").append(dto.getPairNickname()).append("\n\n")
            .append("소요 라운드 : ").append(dto.getTotalRound()).append(" 라운드").append("\n\n")
            .append("결과 : ").append(dto.getResult().getValue()).append("\n\n");

        // 1번 문제 부분
        sb.append("# 1번 문제\n")
            .append("## 문제 : ").append(dto.getTitle1()).append("\n")
            .append(dto.getContent1()).append("\n\n")
            .append("### 제출 코드\n")
            .append("```").append(dto.getLanguage1().toString()).append("\n")
            .append(dto.getCode1()).append("\n")
            .append("```\n\n")
            .append("## 회고 \n")
            .append(dto.getRetro1()).append("\n\n");

        // 2번 문제 부분
        sb.append("# 2번 문제\n")
            .append("## 문제 : ").append(dto.getTitle2()).append("\n")
            .append(dto.getContent2()).append("\n\n")
            .append("### 제출 코드\n")
            .append("```").append(dto.getLanguage2().toString()).append("\n")
            .append(dto.getCode2()).append("\n")
            .append("```\n\n")
            .append("## 회고 \n")
            .append(dto.getRetro2()).append("\n\n");

        return Base64.getEncoder().encodeToString(sb.toString().getBytes());
    }

    public String makeMessage(){
        return "test message";
    }

    // history 제작 완료 후 진행, Lazy Loading 문제 발생 여부 확인 필요
    public String makeMessage(HistoryRetroDto dto, String myNickname){
        String myPairNickname = dto.getManagerNickname().equals(myNickname)? dto.getPairNickname() : dto.getManagerNickname();
        return dto.getResult() + " with " + myPairNickname + " at " + dto.getEndTime().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // 매우 임시로 만든 URL
    public String makeURl(Member member){
        return GITHUB_API_URL + member.getNickname() + "/" + member.getRepository() + "/contents/" + makeDirectoryName() + makeFileName();
    }

    private String makeFileName(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")) + ".md";
    }

    private String makeDirectoryName(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + "/";
    }

}
