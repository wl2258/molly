package kr.co.kumoh.illdang100.mollyspring.dto.surgery;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SurgeryDto {
    private String surgeryName;
    private LocalDate surgeryDate;
}
