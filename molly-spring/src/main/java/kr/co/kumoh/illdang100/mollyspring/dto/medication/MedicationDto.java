package kr.co.kumoh.illdang100.mollyspring.dto.medication;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MedicationDto {
    private String medicationName;
    private LocalDate medicationStartDate;
    private LocalDate medicationEndDate;
}
