package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("R")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rabbit extends Pet{

    @Enumerated(EnumType.STRING)
    private RabbitEnum rabbitSpecies;

    @Builder
    public Rabbit(Long id, Account account, String petName, LocalDate birthdate, PetGenderEnum gender, boolean neuteredStatus, double weight, PetTypeEnum petType, ImageFile petProfileImage, String caution, RabbitEnum rabbitSpecies) {
        super(id, account, petName, birthdate, gender, neuteredStatus, weight, petType, petProfileImage, caution);
        this.rabbitSpecies = rabbitSpecies;
    }
}
