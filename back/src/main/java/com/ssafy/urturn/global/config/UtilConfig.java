package com.ssafy.urturn.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UtilConfig {

    @Value("${grading-server.url}")
    private String url;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(url).build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
