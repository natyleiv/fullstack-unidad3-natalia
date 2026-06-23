package cl.vincalia.catalogo_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
		info = @Info(
				title = "API de Catálogo - FullStack Unidad 3",
				description = "Microservicio encargado de gestionar los productos disponibles y el inventario del catálogo.",
				version = "1.0.0",
				contact = @Contact(
						name = "Vicho",
						email = "vicho@tu-correo.com"
				)
		)
)
@SpringBootApplication
@EnableFeignClients // Como tienes la dependencia de Feign en tu POM, dejamos esto activado
public class CatalogoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogoServiceApplication.class, args);
	}

}