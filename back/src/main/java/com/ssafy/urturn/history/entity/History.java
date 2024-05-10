package com.ssafy.urturn.history.entity;

import static lombok.AccessLevel.PROTECTED;

import com.ssafy.urturn.global.common.BaseEntity;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.problem.Language;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.solving.dto.RetroCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = 0")
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Member manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pair_id", nullable = false)
    private Member pair;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem1_id", nullable = false)
    private Problem problem1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem2_id", nullable = false)
    private Problem problem2;

    @Column(length = 5000)
    private String code1;

    @Column(length = 5000)
    private String code2;

    private Language language1;
    private Language language2;

    private HistoryResult result;
    @Column(length = 500)
    private String retro1;
    @Column(length = 500)
    private String retro2;
    private int totalRound;
    @Column(name="end_time")
    private LocalDateTime endTime;

    public int setCode(Long problemId, String code, Language language){
        if (Objects.equals(this.problem1.getId(), problemId)){
            this.code1 = code;
            this.language1 = language;
            return 1;
        } else if (Objects.equals(this.problem2.getId(), problemId)) {
            this.code2 = code;
            this.language2 = language;
            return 2;
        } else {
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR, "해당 id에 해당하는 문제가 history에 존재하지 않습니다.");
        }
    }

    public void setRetro(RetroCreateRequest req){
        this.retro1 = req.getRetro1();
        this.retro2 = req.getRetro2();
    }

    public void finalizeUpdateHistory(HistoryResult result, int totalRound){
        this.endTime=LocalDateTime.now();
        this.result=result;
        this.totalRound=totalRound;
    }
}
