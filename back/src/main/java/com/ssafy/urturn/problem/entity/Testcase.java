package com.ssafy.urturn.problem.entity;

import static lombok.AccessLevel.PROTECTED;

import com.ssafy.urturn.global.common.BaseEntity;
import com.ssafy.urturn.problem.Language;
import com.ssafy.urturn.problem.dto.TestcaseCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Testcase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;
    private String stdin;
    @Column(name = "expected_output")
    private String expectedOutput;

    @Column(name="is_public")
    private boolean isPublic;

    public Testcase(TestcaseCreateRequest req, Problem problem){
        this.problem = problem;
        this.language = req.getLanguage();
        this.stdin = req.getStdin();
        this.expectedOutput = req.getExpectedOutput();
        this.isPublic = req.isPublic();
    }

}
