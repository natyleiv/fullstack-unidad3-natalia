package cl.vincalia.catalogo_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server gatewayServer = new Server();
        // Asegúrate de que este sea el puerto real de tu API Gateway
        gatewayServer.setUrl("http://localhost:9090");
        gatewayServer.setDescription("API Gateway");

        return new OpenAPI().servers(List.of(gatewayServer));
    }
}