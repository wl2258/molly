package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("Cat")
public class Cat extends Pet{

    @Enumerated(EnumType.STRING)
       private CatEnum species;
}
