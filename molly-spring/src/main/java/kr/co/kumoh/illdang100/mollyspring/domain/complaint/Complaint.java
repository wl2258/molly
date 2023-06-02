package kr.co.kumoh.illdang100.mollyspring.domain.complaint;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Complaint extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long id;

    @Column(nullable = false, length = 40)
    private String reporterEmail;

    private Long boardId;
    private Long commentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private ComplaintReasonEnum complaintReason;
}
