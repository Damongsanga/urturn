package com.ssafy.urturn.global.util;

import static com.ssafy.urturn.global.exception.errorcode.CommonErrorCode.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.urturn.global.exception.RestApiException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil(){
        throw new IllegalStateException("Utility class");
    }

    public static String convertObjectToJson(Object obj){
        try{
            return objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            throw new RestApiException(INTERNAL_SERVER_ERROR);
        }
    }
}
