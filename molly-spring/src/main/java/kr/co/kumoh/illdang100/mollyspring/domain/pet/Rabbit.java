package kr.co.kumoh.illdang100.mollyspring.domain.pet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("Rabbit")
public class Rabbit extends Pet{

    @Enumerated(EnumType.STRING)
    private RabbitEnum species;
}
