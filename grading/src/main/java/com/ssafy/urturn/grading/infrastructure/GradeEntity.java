package com.ssafy.urturn.grading.infrastructure;

import com.ssafy.urturn.grading.domain.Grade;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "grade", timeToLive = 3600)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GradeEntity {
    @Id
    private String token;
    private int languageId;
    private String stdout;
    private int statusId;
    private String stderr;

    public static GradeEntity from(Grade grade){
        GradeEntity gradeEntity = new GradeEntity();
        gradeEntity.languageId = grade.getLanguageId();
        gradeEntity.stdout = grade.getStdout();
        gradeEntity.statusId = grade.getStatusId();
        gradeEntity.stderr = grade.getStderr();
        gradeEntity.token = grade.getToken();
        return gradeEntity;
    }

    public Grade toModel(){
        return Grade.builder()
                .languageId(this.languageId)
                .statusId(this.statusId)
                .stdout(this.stdout)
                .stderr(this.stderr)
                .token(this.token)
                .build();
    }
}
