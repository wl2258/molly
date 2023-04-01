package kr.co.kumoh.illdang100.mollyspring.repository.medication;

import kr.co.kumoh.illdang100.mollyspring.domain.medication.MedicationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<MedicationHistory, Long> {

    List<MedicationHistory> findByPetId(Long petId);
}
