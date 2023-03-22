package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("Dog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dog extends Pet{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DogEnum dogSpecies;

    @Builder
    public Dog(Long id, Account account, String petName, LocalDate birthdate, PetGenderEnum gender, boolean neuteredStatus, double weight, PetTypeEnum petType, ImageFile petProfile, DogEnum species) {
        super(id, account, petName, birthdate, gender, neuteredStatus, weight, petType, petProfile);
        this.dogSpecies = species;
    }
}
