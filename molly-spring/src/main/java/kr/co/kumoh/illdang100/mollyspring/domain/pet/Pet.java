package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetReqDto.PetUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 1)
@AllArgsConstructor
public abstract class Pet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, length = 30)
    private String petName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PetGenderEnum gender;

    @Column(nullable = false)
    private boolean neuteredStatus;

    @Column(nullable = false)
    private double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private PetTypeEnum petType;

    @Embedded
    private ImageFile petProfileImage;

    @Column(length = 100)
    private String caution;

    public boolean comparePetName(String petName) {
        if (this.petName != petName) return false;
        return true;
    }

    public boolean compareBirthDate(LocalDate birthDate) {
        if (this.birthDate != birthDate) return false;
        return true;
    }

    public boolean compareGender(PetGenderEnum gender) {
        if (this.gender != gender) return false;
        return true;
    }

    public boolean compareNeuteredStatus(boolean neuteredStatus) {
        if (this.neuteredStatus != neuteredStatus) return false;
        return true;
    }

    public boolean compareWeight(Double weight) {
        if (this.weight != weight) return false;
        return true;
    }

    public boolean compareCaution(String caution) {
        if (this.caution != caution) return false;
        return true;
    }

    public void updatePetProfileImage(ImageFile petProfileImage){
        this.petProfileImage = petProfileImage;
    }

    public void updatePet(PetUpdateRequest petInfo) {
        if (!comparePetName(petInfo.getPetName())) this.petName = petInfo.getPetName();
        if (!compareBirthDate(petInfo.getBirthDate())) this.birthDate = petInfo.getBirthDate();
        if (!compareNeuteredStatus(petInfo.isNeuteredStatus())) this.neuteredStatus = petInfo.isNeuteredStatus();
        if (!compareGender(petInfo.getGender())) this.gender = petInfo.getGender();
        if (!compareWeight(petInfo.getWeight())) this.weight = petInfo.getWeight();
        if (!compareCaution(petInfo.getCaution())) this.caution = petInfo.getCaution();
    }
}
