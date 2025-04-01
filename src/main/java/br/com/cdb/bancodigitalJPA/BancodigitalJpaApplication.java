package br.com.cdb.bancodigitalJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//quando usamos JPA, preciso ir no application.properties, para "configurações"

@SpringBootApplication
public class BancodigitalJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancodigitalJpaApplication.class, args);
	}

}