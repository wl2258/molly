package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("D")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dog extends Pet{

    @Enumerated(EnumType.STRING)
    private DogEnum dogSpecies;

    @Builder
    public Dog(Long id, Account account, String petName, LocalDate birthdate, PetGenderEnum gender, boolean neuteredStatus, double weight, PetTypeEnum petType, ImageFile petProfileImage, String caution, DogEnum dogSpecies) {
        super(id, account, petName, birthdate, gender, neuteredStatus, weight, petType, petProfileImage, caution);
        this.dogSpecies = dogSpecies;
    }
}
