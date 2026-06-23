package cl.vincalia.pagos_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient          // ← para registrar en Eureka
@EnableFeignClients             // ← para escanear interfaces Feign
public class PagosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagosServiceApplication.class, args);
	}
}