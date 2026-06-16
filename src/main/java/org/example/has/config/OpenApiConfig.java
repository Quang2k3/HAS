package org.example.has.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    /**
 * Creates the OpenAPI configuration bean.
 *
 * @return the OpenAPI configuration object
 */
    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    /**
 * Creates the OpenAPI configuration bean.
 *
 * @return the OpenAPI configuration object
 */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hospital Appointment System API")
                        .description("Hệ thống quản lý đặt lịch khám bệnh — "
                                + "REST API cho quản lý khoa, bác sĩ, bệnh nhân, lịch hẹn, "
                                + "hồ sơ khám bệnh và báo cáo thống kê.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HAS Development Team")
                                .email("admin@hospital.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))

                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập JWT token (không cần prefix 'Bearer ')")
                        ));
    }
}
