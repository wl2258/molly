package kr.co.kumoh.illdang100.mollyspring.domain.vaccinations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DogVaccinationEnum {

    DHPPL("종합백신"),

    CORONAVIRUS("코로나 장염"),

    KENNEL_COUGH("켄넬코프"),

    INFLUENZA("인플루엔자"),

    DOG_RABIES("광견병"),

    D0G_ANTIBODY_TITER_TEST("항체가검사");

    private String  value;
}
