package com.ssafy.urturn.problem.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubmissionDto {
    private int languageId;
    @Setter
    private String stdout;
    private int statusId;
    @Setter
    private String stderr;
    private String token;

    // stderr 필드를 위한 디코더 메소드
    public void decodeBase64toUTF8() {
        if (this.stdout != null) {
            this.stdout = new String(Base64.getDecoder().decode(this.stdout.replaceAll("\\s+", "")),
                StandardCharsets.UTF_8);
        }
        if (this.stderr != null) {
            this.stderr = new String(Base64.getDecoder().decode(this.stderr.replaceAll("\\s+", "")),
                StandardCharsets.UTF_8);
        }
    }

}
