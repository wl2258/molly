package kr.co.kumoh.illdang100.mollyspring;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class MollySpringApplication {

	private final AccountRepository accountRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	@Value("${admin.name}")
	private String adminUsername;

	@Value("${admin.password}")
	private String adminPassword;

	@Value("${admin.email}")
	private String adminEmail;

	@Value("${admin.nickname}")
	private String adminNickname;

	public static void main(String[] args) {
		SpringApplication.run(MollySpringApplication.class, args);
	}

	@PostConstruct
	public void init() {

		Account adminAccount = Account.builder()
				.id(1L)
				.username(adminUsername)
				.password(passwordEncoder.encode(adminPassword))
				.email(adminEmail)
				.role(AccountEnum.ADMIN)
				.nickname(adminNickname)
				.build();

		Optional<Account> findAccount = accountRepository.findByUsername(adminUsername);

		if (findAccount.isEmpty()) {
			accountRepository.save(adminAccount);
		}
	}
}
