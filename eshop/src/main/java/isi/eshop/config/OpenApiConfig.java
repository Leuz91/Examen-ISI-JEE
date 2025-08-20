package isi.eshop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Eshop API", version = "v1", description = "TP1 – Eshop REST Level 3")
)
@Configuration
public class OpenApiConfig {

}
