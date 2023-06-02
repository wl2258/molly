package kr.co.kumoh.illdang100.mollyspring.repository.suspension;

import kr.co.kumoh.illdang100.mollyspring.domain.suspension.SuspensionDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuspensionDateRepository extends JpaRepository<SuspensionDate, Long> {

    Optional<SuspensionDate> findByAccountEmail(String accountEmail);
}