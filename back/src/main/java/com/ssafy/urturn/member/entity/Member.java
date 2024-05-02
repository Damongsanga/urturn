package com.ssafy.urturn.member.entity;

import static lombok.AccessLevel.PROTECTED;

import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.auth.dto.OAuthAccessTokenResponse;
import com.ssafy.urturn.global.common.BaseEntity;
import com.ssafy.urturn.member.Level;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = 0")
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(name= "profile_image", nullable = false)
    private String profileImage;
    private String email;
    private String repository;

    @Column(name= "github_access_token")
    private String githubAccessToken;

    @Column(nullable = false)
    private int exp;
    @Column(nullable = false)
    private Level level;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public static Member newMember(String nickname){
        return Member.builder()
            .nickname(nickname)
            .profileImage("testProfileImage")
            .level(Level.LEVEL1).build();
    }

    public void updateGithubRepository(String repository){
        this.repository = repository;
    }

    public void updateGithubTokens(String githubAccessToken){
        this.githubAccessToken = githubAccessToken;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
            .map((r) -> new SimpleGrantedAuthority(r.name()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id+"";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
