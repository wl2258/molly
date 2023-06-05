package kr.co.kumoh.illdang100.mollyspring.domain.liky;

import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Liky {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liky_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    @Column(nullable = false, length = 45)
    private String accountEmail;

    public Liky(Board board, String accountEmail) {
        this.board = board;
        this.accountEmail = accountEmail;
    }
}
