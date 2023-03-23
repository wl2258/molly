package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("C")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cat extends Pet{

    @Enumerated(EnumType.STRING)
    private CatEnum catSpecies;

    @Builder
    public Cat(Long id, Account account, String petName, LocalDate birthdate, PetGenderEnum gender, boolean neuteredStatus, double weight, PetTypeEnum petType, ImageFile petProfileImage, CatEnum catSpecies) {
        super(id, account, petName, birthdate, gender, neuteredStatus, weight, petType, petProfileImage);
        this.catSpecies = catSpecies;
    }
}
