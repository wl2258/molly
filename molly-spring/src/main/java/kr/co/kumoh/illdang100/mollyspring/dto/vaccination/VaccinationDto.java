package kr.co.kumoh.illdang100.mollyspring.dto.vaccination;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VaccinationDto {
    private String  vaccinationName;
    private LocalDate vaccinationDate;
}
