package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
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
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PetGenderEnum gender;

    @Column(nullable = false)
    private boolean neuteredStatus;

    @Column(nullable = false)
    private double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PetTypeEnum petType;

    @Embedded
    private ImageFile petProfileImage;

    @Column(length = 100)
    private String caution;

    public Pet(Long id, Account account, String petName, LocalDate birthdate, PetGenderEnum gender, boolean neuteredStatus, double weight, PetTypeEnum petType, ImageFile petProfileImage, String caution) {
        this.id = id;
        this.account = account;
        this.petName = petName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.neuteredStatus = neuteredStatus;
        this.weight = weight;
        this.petType = petType;
        this.petProfileImage = petProfileImage;
        this.caution = caution;
    }
}
