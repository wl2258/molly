package kr.co.kumoh.illdang100.mollyspring.repository.account;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
