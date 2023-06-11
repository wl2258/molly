package kr.co.kumoh.illdang100.mollyspring.domain.suspension;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.ComplaintReasonEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Suspension extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suspension_id")
    private Long id;

    @Column(nullable = false, length = 45)
    private String accountEmail;
    @Column(unique = true)
    private Long boardId;
    @Column(unique = true)
    private Long commentId;

    @Column(nullable = false)
    private Long suspensionPeriod;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private ComplaintReasonEnum complaintReason;
}