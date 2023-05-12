package kr.co.kumoh.illdang100.mollyspring.domain.comment;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    // TODO: 사용자 닉네임 또한 마찬가지
    @Column(nullable = false, length = 20)
    private String writerNickname;

    // TODO: 사용자 프로필 이미지 필드 없애고 조회할 때마다 사용자 프로필 이미지 별도로 조회해오기
    private String writerProfileUrl;
}
