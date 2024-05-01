package com.ssafy.urturn.problem.entity;


import static lombok.AccessLevel.PROTECTED;

import com.ssafy.urturn.global.common.BaseEntity;
import com.ssafy.urturn.member.Level;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
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
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Level level;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<Testcase> testcases;

}
