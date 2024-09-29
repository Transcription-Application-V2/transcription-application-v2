package project.transcription_application_v2.infrastructure.openAi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("public-api")
        .pathsToMatch("/api/**")
        .build();
  }

  @Bean
  OpenAPI springDocOpenApi() {
    return new OpenAPI()
        .info(new Info().title("Transcription Application API")
            .description("API for transcribing audio files")
            .version("v0.0.1-SNAPSHOT"))
        .components(new io.swagger.v3.oas.models.Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
  }
}
