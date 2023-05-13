package kr.co.kumoh.illdang100.mollyspring.domain.vaccinations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CatVaccinationEnum {
    CVRP("종합백신"),

    CAT_RABIES("광견병"),

    FIP("전염성 복막염"),

    CAT_ANTIBODY_TITER_TEST("항체가검사");
    private String value;
}
