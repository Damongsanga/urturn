package com.ssafy.urturn.problem;

import com.ssafy.urturn.problem.dto.TokenDto;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum Grading {

    CREATE_TOKENS("submissions/batch", new String[] {"base64_encoded=false"}, String.class),
    GET_GRADES("/submissions/batch", new String[] {"tokens=","&fields=token,stdout,stderr,status_id,language_id","&base64_encoded=false"}, String.class);

    private final String path;
    private final String[] query;

    private final Class<?> rtnClass;

    Grading(String path, String[] query, Class<?> rtnClass) {
        this.path = path;
        this.query = query;
        this.rtnClass = rtnClass;
    }

    public String getQuery(List<TokenDto> token){
        List<String> tokenStringList = token.stream().map(TokenDto::getToken).toList();
        if (!this.name().equals("GET_GRADES")) return query[0];
        return query[0] + String.join(",", tokenStringList) + query[1] +query[2];
    }

    public String getQuery(){
        return query[0];
    }
}
