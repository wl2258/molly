package kr.co.kumoh.illdang100.mollyspring.repository;

import kr.co.kumoh.illdang100.mollyspring.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
