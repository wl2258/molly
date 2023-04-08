package kr.co.kumoh.illdang100.mollyspring.domain.board;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(nullable = false, length = 1000)
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
    private boolean isNotice;

    @Builder
    public Board(Long id, Account account, String title, String content, BoardEnum category, PetTypeEnum petType, int views) {
        this.id = id;
        this.account = account;
        this.boardTitle = title;
        this.boardContent = content;
        this.category = category;
        this.petType = petType;
        this.views = views;
    }
}
