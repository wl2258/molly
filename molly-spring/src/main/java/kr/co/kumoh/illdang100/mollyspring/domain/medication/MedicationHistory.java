package kr.co.kumoh.illdang100.mollyspring.domain.medication;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.Pet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(nullable = false)
    private LocalDate medicationDate;

    @Column(nullable = false, length = 30)
    private String medicationName;

    @Builder
    public MedicationHistory(Long id, Pet pet, LocalDate medicationDate, String medicationName) {
        this.id = id;
        this.pet = pet;
        this.medicationDate = medicationDate;
        this.medicationName = medicationName;
    }
}
