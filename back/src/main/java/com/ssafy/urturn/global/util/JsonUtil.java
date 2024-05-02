package com.ssafy.urturn.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String convertObjectToJson(Object obj){
        try{
            return objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException("Jsom 변환 실패 ", e);
        }
    }
}
