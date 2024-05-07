package com.ssafy.urturn.problem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.urturn.member.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProblemCreateRequest {
    private String title;
    private String content;
    private Level level;
}
