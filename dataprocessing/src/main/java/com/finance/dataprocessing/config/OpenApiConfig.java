package com.finance.dataprocessing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI financeApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Finance Data Processing API")
                        .description("Backend APIs for financial records, users, and analytics dashboard")
                        .version("1.0"));
    }
}
