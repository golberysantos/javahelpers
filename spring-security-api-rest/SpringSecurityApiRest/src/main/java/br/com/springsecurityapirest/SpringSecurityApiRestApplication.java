package br.com.springsecurityapirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Classe principal para inicialização da API segura.
 *
 * @author Golbery Santos
 */
@SpringBootApplication
@EnableFeignClients
public class SpringSecurityApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApiRestApplication.class, args);
	}

}
