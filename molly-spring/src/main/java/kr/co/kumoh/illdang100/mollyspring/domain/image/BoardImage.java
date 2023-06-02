package kr.co.kumoh.illdang100.mollyspring.domain.image;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
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

    private Long boardId;

    @Embedded
    @Column(nullable = false)
    private ImageFile boardImageFile;

    public BoardImage(Long boardId, ImageFile boardImageFile) {
        this.boardId = boardId;
        this.boardImageFile = boardImageFile;
    }

    public void changeBoardId(Long boardId) {
        this.boardId = boardId;
    }
}
