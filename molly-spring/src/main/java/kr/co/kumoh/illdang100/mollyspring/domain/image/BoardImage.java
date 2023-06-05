package kr.co.kumoh.illdang100.mollyspring.domain.image;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Embedded
    @Column(nullable = false)
    private ImageFile boardImageFile;

    public BoardImage(Board board, ImageFile boardImageFile) {
        this.board = board;
        this.boardImageFile = boardImageFile;
    }

    public void changeBoard(Board board) {
        this.board = board;
    }
}
