package tmrv.dev.blogsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Blog System",
                version = "v3",
                contact = @Contact(
                        name = "Temirov Jaloliddin",
                        email = "jaloliddintemirov890@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://springdoc.org"
                ),
                description = "This System include some kind of features."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Production-Server"
                )
        },
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"

)
public class SwaggerConfig {


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public GroupedOpenApi admin() {
        return GroupedOpenApi.builder()
                .group("Admin APIs")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi customer() {
        return GroupedOpenApi.builder()
                .group("User APIs")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi both() {
        return GroupedOpenApi.builder()
                .group("User and Admin APIs")
                .pathsToMatch("/both/**")
                .build();
    }

    @Bean
    public GroupedOpenApi noAuthenticationApi() {
        return GroupedOpenApi.builder()
                .group("No Auth APIs")
                .pathsToMatch("/register/**", "/login/**")
                .build();
    }


}
