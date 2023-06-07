package kr.co.kumoh.illdang100.mollyspring.security.dummy;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.SuspensionDate;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.liky.LikyRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.suspension.SuspensionDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DummyDevInit extends DummyObject {

    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.name}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.nickname}")
    private String adminNickname;

    @Profile("dev") // prod 모드에서는 실행되면 안된다.
    @Bean
    CommandLineRunner init(AccountRepository accountRepository, BoardRepository boardRepository,
                           CommentRepository commentRepository, LikyRepository likyRepository,
                           SuspensionDateRepository suspensionDateRepository) {
        return (args) -> {
            // 서버 실행시 무조건 실행된다.

            Account admin = accountRepository.save(Account.builder()
                    .id(1L)
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .role(AccountEnum.ADMIN)
                    .nickname(adminNickname)
//                    .accountProfileImage(imageFile)
                    .build());

            Board board = boardRepository.save(Board.builder()
                    .account(admin)
                            .accountEmail(admin.getEmail())
                    .boardTitle("testTitle")
                    .boardContent("<p>testContent</p>")
                    .category(BoardEnum.FREE)
                    .petType(PetTypeEnum.DOG)
                    .views(193)
                    .likyCnt(100)
                    .commentCnt(10)
                    .hasImage(true)
                    .isNotice(true)
                    .build());

            likyRepository.save(new Liky(board, "test1L@naver.com"));
            likyRepository.save(new Liky(board, "test2L@naver.com"));

            for (int i = 0; i < 5; i++) {
                commentRepository.save(new Comment(board, "testCommentContent" + i, "testEmail" + i));

                try {
                    Thread.sleep(1000); // 1초 동안 멈춤
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Account jjanggu = accountRepository.save(newAccount("kakao_1234", "짱구"));
            Account yuli = accountRepository.save(newAccount("kakao_5678", "유리"));
            Account cheolsu = accountRepository.save(newAccount("kakao_9101", "철수"));
            Account maenggu = accountRepository.save(newAccount("kakao_9999", "맹구"));

            Board jjangguBoard = boardRepository.save(newBoard(jjanggu, "jjangguPost", "<p>jjangguContent</p>", BoardEnum.FREE,
                    PetTypeEnum.DOG, false));
            Board yuliBoard = boardRepository.save(newBoard(yuli, "yuliPost", "<p>yuliContent</p>", BoardEnum.FREE,
                    PetTypeEnum.RABBIT, false));
            Board cheolsuBoard = boardRepository.save(newBoard(cheolsu, "cheolsuPost", "<p>cheolsuContent</p>", BoardEnum.MEDICAL,
                    PetTypeEnum.CAT, true));
            Board maengguBoard = boardRepository.save(newBoard(maenggu, "maengguPost", "<p>maengguContent</p>", BoardEnum.MEDICAL,
                    PetTypeEnum.CAT, true));

            boardRepository.save(Board.builder()
                    .account(null)
                            .accountEmail("testnull@naver.com")
                            .boardTitle("사용자 없음")
                            .boardContent("boardContent")
                            .category(BoardEnum.FREE)
                            .petType(PetTypeEnum.DOG)
                            .views(0)
                            .likyCnt(0)
                            .commentCnt(0)
                            .hasImage(false)
                            .isNotice(false)
                    .build());

            likyRepository.save(newLiky(jjangguBoard, yuli.getEmail(), boardRepository));
            likyRepository.save(newLiky(jjangguBoard, cheolsu.getEmail(), boardRepository));
            likyRepository.save(newLiky(jjangguBoard, "test100L@naver.com", boardRepository));
            likyRepository.save(newLiky(jjangguBoard, "test101L@naver.com", boardRepository));
            likyRepository.save(newLiky(yuliBoard, jjanggu.getEmail(), boardRepository));
            likyRepository.save(newLiky(yuliBoard, cheolsu.getEmail(), boardRepository));
            likyRepository.save(newLiky(cheolsuBoard, jjanggu.getEmail(), boardRepository));
            likyRepository.save(newLiky(cheolsuBoard, yuli.getEmail(), boardRepository));
            likyRepository.save(newLiky(cheolsuBoard, "test100L@naver.com", boardRepository));
            likyRepository.save(newLiky(maengguBoard, jjanggu.getEmail(), boardRepository));
            likyRepository.save(newLiky(maengguBoard, yuli.getEmail(), boardRepository));

            commentRepository.save(newComment(jjangguBoard, "짱구야 소꿉놀이 하자.", yuli.getEmail(), boardRepository));
            commentRepository.save(newComment(jjangguBoard, "싫어 나 액션가면 봐야해.", jjanggu.getEmail(), boardRepository));
            commentRepository.save(newComment(jjangguBoard, "바보", yuli.getEmail(), boardRepository));
            commentRepository.save(newComment(jjangguBoard, "메롱", jjanggu.getEmail(), boardRepository));
            commentRepository.save(newComment(jjangguBoard, "짱구야 공부하자.", cheolsu.getEmail(), boardRepository));
            commentRepository.save(newComment(jjangguBoard, "부리부리 부리부리", jjanggu.getEmail(), boardRepository));

            commentRepository.save(newComment(yuliBoard, "유리야 그거 재밌어?", jjanggu.getEmail(), boardRepository));
            commentRepository.save(newComment(yuliBoard, "응 다음에 짱구도 같이 하자.", yuli.getEmail(), boardRepository));
            commentRepository.save(newComment(yuliBoard, "내일보자 유리야", cheolsu.getEmail(), boardRepository));
            commentRepository.save(newComment(yuliBoard, "그래", yuli.getEmail(), boardRepository));

            commentRepository.save(newComment(cheolsuBoard, "철수야 소꿉놀이 하자.", yuli.getEmail(), boardRepository));
            commentRepository.save(newComment(cheolsuBoard, "안돼 나 학원 가야 할 시간이야.", cheolsu.getEmail(), boardRepository));
            commentRepository.save(newComment(cheolsuBoard, "철수야 사랑해", jjanggu.getEmail(), boardRepository));
            commentRepository.save(newComment(cheolsuBoard, "으~ 징그러", cheolsu.getEmail(), boardRepository));
            commentRepository.save(newComment(cheolsuBoard, "히히 철수도 좋으면서", jjanggu.getEmail(), boardRepository));
        };
    }
}
