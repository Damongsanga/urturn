package com.ssafy.urturn.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		final String securitySchemeName = "bearerAuth";
		final String moduleName = "urturn";
		final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));

		// 테스트할 서버 세팅
		Server localServer = new Server();
		localServer.setDescription("local");
		localServer.setUrl("http://localhost:8080");

		Server testServer = new Server();
		testServer.setDescription("server");
		testServer.setUrl("https://urturn.site/api");

		OpenAPI openAPI = new OpenAPI()
			.components(new Components()
				.addSecuritySchemes(securitySchemeName,
					new SecurityScheme()
						.name(securitySchemeName)
						.type(Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")))

			.security(List.of(
				new SecurityRequirement().addList("bearerAuth")
			))

			.info(new Info()
				.title(apiTitle)
				.description("어마어마하게 재밌고 유용한 페어프로그래밍 서비스입니다")
				.version("1.0.0"));

		openAPI.setServers(Arrays.asList(localServer, testServer));

		return openAPI;


	}
}
