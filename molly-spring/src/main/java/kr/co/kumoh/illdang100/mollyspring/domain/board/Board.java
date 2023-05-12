package kr.co.kumoh.illdang100.mollyspring.domain.board;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import lombok.*;

import javax.persistence.*;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, length = 60)
    private String boardTitle;

    @Column(nullable = false, length = 2000)
    private String boardContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private BoardEnum category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private PetTypeEnum petType;

    @Column(nullable = false)
    private long views;

    @Column(nullable = false)
    private long likyCnt;

    @Column(nullable = false)
    private long commentCnt;

    @Column(nullable = false)
    private boolean hasImage;

    @Column(nullable = false)
    private boolean isNotice;

    public Board(Account account, CreatePostRequest createPostRequest) {
        this.account = account;
        this.boardTitle = createPostRequest.getTitle();
        this.boardContent = createPostRequest.getContent();
        this.category = BoardEnum.valueOf(createPostRequest.getCategory());
        this.petType = PetTypeEnum.valueOf(createPostRequest.getPetType());
        this.views = 0;
        this.likyCnt = 0;
        this.commentCnt = 0;
        this.hasImage = false;
        this.isNotice = false;
    }

    public void changeHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public void increaseViews() {
        views++;
    }
}
