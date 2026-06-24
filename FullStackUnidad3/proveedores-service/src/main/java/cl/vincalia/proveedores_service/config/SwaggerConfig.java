package cl.vincalia.proveedores_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Proveedores Service API")
                        .version("1.0")
                        .description("Microservicio de gestión de proveedores - FullStack Unidad 3")
                        .contact(new Contact()
                                .name("Natalia Leiva")
                                .email("natalia.leiva@duocuc.cl")));
    }
}
