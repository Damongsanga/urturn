package com.ssafy.urturn.global.util;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_USER;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class MemberUtil {
    public static Long getMemberId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        try {
            return Long.parseLong(userDetails.getUsername());
        } catch(NumberFormatException e) {
            throw new RestApiException(NO_USER);
        }
    }
}
