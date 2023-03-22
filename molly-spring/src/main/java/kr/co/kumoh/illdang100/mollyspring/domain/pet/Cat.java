package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("Cat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cat extends Pet{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CatEnum catSpecies;

    @Builder
    public Cat(Long id, Account account, String petName, LocalDate birthdate, PetGenderEnum gender, boolean neuteredStatus, double weight, PetTypeEnum petType, ImageFile petProfile, CatEnum species) {
        super(id, account, petName, birthdate, gender, neuteredStatus, weight, petType, petProfile);
        this.catSpecies = species;
    }
}
