package kr.co.kumoh.illdang100.mollyspring.domain.complaint;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardComplaint extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_complaint__id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 45)
    private String reporterEmail;

    @Column(nullable = false, length = 45)
    private String reportedEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private ComplaintReasonEnum complaintReason;
}
