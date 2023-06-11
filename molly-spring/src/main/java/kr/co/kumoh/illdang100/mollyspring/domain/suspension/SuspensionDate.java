package kr.co.kumoh.illdang100.mollyspring.domain.suspension;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SuspensionDate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suspension_date_id")
    private Long id;

    @Column(nullable = false, length = 45)
    private String accountEmail;
    @Column(nullable = false)
    private LocalDate suspensionExpiryDate;

    public SuspensionDate(String accountEmail, LocalDate suspensionExpiryDate) {
        this.accountEmail = accountEmail;
        this.suspensionExpiryDate = suspensionExpiryDate;
    }

    public void changeSuspensionExpiryDate(LocalDate suspensionExpiryDate) {
        this.suspensionExpiryDate = suspensionExpiryDate;
    }
}
