package kr.co.kumoh.illdang100.mollyspring.domain.board;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(nullable = false)
    private String uploadFileName;

    @Column(nullable = false)
    private String storeFileName;

    @Builder
    public BoardImage(Long id, Board board, String uploadFileName, String storeFileName) {
        this.id = id;
        this.board = board;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
