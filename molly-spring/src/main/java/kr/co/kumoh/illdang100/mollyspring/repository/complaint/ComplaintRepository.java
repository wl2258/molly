package kr.co.kumoh.illdang100.mollyspring.repository.complaint;

import kr.co.kumoh.illdang100.mollyspring.domain.complaint.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}
