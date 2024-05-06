package com.ssafy.urturn.history.entity;

import static lombok.AccessLevel.PROTECTED;

import com.ssafy.urturn.global.common.BaseEntity;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.problem.entity.Problem;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String code1;
    private String code2;
    private HistoryResult result;
    private String retro1;
    private String retro2;
    private int totalRound;
    @Column(name="end_time")
    private LocalDateTime endTime;

}
