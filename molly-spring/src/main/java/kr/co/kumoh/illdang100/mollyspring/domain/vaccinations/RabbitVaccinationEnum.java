package kr.co.kumoh.illdang100.mollyspring.domain.vaccinations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RabbitVaccinationEnum {

    RVH("유행성 출혈병"),

    RABBIT_RABIES("광견병");
    private String  value;
}
