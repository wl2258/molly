package kr.co.kumoh.illdang100.mollyspring.domain.liky;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Liky {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liky_id")
    private Long id;
}
