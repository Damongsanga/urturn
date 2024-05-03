package com.ssafy.urturn.github;

import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.member.entity.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class GithubUploadUtil {

    static final String GITHUB_API_URL = "https://api.github.com/repos/";

    public String makeContent(History history){
        String originalString = "test String I like you!";
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }

    public String makeMessage(){
        return "test message";
    }

    // history 제작 완료 후 진행, Lazy Loading 문제 발생 여부 확인 필요
    public String makeMessage(History history){
        return history.getResult() + " with " + history.getPair().getNickname() + " at " + history.getEndTime().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // 매우 임시로 만든 URL
    public String makeURl(Member member){
        return GITHUB_API_URL + member.getNickname() + "/" + member.getRepository() + "/contents/" + makeFileName();
    }

    private String makeFileName(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")) + ".md";
    }

}
