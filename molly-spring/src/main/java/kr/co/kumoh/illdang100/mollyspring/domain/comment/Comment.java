package kr.co.kumoh.illdang100.mollyspring.domain.comment;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false, length = 800)
    private String commentContent;

    @Column(nullable = false)
    private Long accountId;

    public Comment(Board board, String commentContent, Long accountId) {
        this.board = board;
        this.commentContent = commentContent;
        this.accountId = accountId;
    }
}
