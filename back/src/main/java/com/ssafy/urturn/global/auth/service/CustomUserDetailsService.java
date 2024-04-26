package com.ssafy.urturn.global.auth.service;


import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return memberRepository.findById(Long.parseLong(memberId))
                .map(this::createUserDetails)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.NO_MEMBER));
    }

    // 멤버 정보를 UserDetail 객체로 변경하여 return
    private UserDetails createUserDetails(Member member) {
        String stringRole = member.getRoles().toArray(new Role[0])[0].name();
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(stringRole)
                .build();
    }
}
