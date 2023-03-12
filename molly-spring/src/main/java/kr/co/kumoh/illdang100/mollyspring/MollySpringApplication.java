package kr.co.kumoh.illdang100.mollyspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MollySpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MollySpringApplication.class, args);
	}

}
