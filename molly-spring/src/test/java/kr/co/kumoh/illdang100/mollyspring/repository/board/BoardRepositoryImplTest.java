package kr.co.kumoh.illdang100.mollyspring.repository.board;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.liky.LikyRepository;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class BoardRepositoryImplTest extends DummyObject {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikyRepository likyRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        autoIncrementReset();
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    public void findPagePostList_all_createdDate_desc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("ALL", "ALL", null);
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdDate"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();

        // then
        assertThat(content.get(0).getWriterNick()).isEqualTo("맹구");
        assertThat(content.get(1).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(2).getWriterNick()).isEqualTo("유리");
        assertThat(content.get(3).getWriterNick()).isEqualTo("짱구");
    }

    @Test
    public void findPagePostList_all_likyCnt_desc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("ALL", "ALL", null);
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "likyCnt"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();

        // then
        assertThat(content.get(0).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(1).getWriterNick()).isEqualTo("맹구");
        assertThat(content.get(2).getWriterNick()).isEqualTo("짱구");
        assertThat(content.get(3).getWriterNick()).isEqualTo("유리");
    }

    @Test
    public void findPagePostList_all_views_desc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("ALL", "ALL", null);
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "views"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();
        content.forEach(b -> {
            System.out.println("b.getTitle() = " + b.getTitle());
            System.out.println("b.getViews() = " + b.getViews());
        });

        // then
        assertThat(content.size()).isEqualTo(3);
        assertThat(content.get(0).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(1).getWriterNick()).isEqualTo("맹구");
        assertThat(content.get(2).getWriterNick()).isEqualTo("짱구");
    }

    @Test
    public void findPagePostList_all_views_asc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("ALL", "ALL", null);
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.ASC, "views"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();

        // then
        assertThat(content.get(0).getWriterNick()).isEqualTo("맹구");
        assertThat(content.get(1).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(2).getWriterNick()).isEqualTo("유리");
        assertThat(content.get(3).getWriterNick()).isEqualTo("짱구");
    }

    @Test
    public void findPagePostList_all_commentCnt_desc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("ALL", "ALL", null);
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "commentCnt"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();

        // then
        assertThat(content.get(0).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(1).getWriterNick()).isEqualTo("맹구");
        assertThat(content.get(2).getWriterNick()).isEqualTo("짱구");
        assertThat(content.get(3).getWriterNick()).isEqualTo("유리");
    }

    @Test
    public void findPagePostList_medical_commentCnt_desc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("MEDICAL", "ALL", null);
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "commentCnt"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();

        // then
        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(1).getWriterNick()).isEqualTo("맹구");
    }

    @Test
    public void findPagePostList_cat_commentCnt_desc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("ALL", "CAT", null);
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "commentCnt"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();

        // then
        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(1).getWriterNick()).isEqualTo("맹구");
    }

    @Test
    public void findPagePostList_medical_cat_commentCnt_desc_test() {

        // given
        BoardReqDto.RetrievePostListCondition retrievePostListCondition =
                new BoardReqDto.RetrievePostListCondition("MEDICAL", "CAT", null);
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "commentCnt"));

        // when
        List<RetrievePostListDto> content =
                boardRepository.findPagePostList(retrievePostListCondition, pageRequest)
                        .getContent();

        // then
        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).getWriterNick()).isEqualTo("철수");
        assertThat(content.get(1).getWriterNick()).isEqualTo("맹구");
    }

    private void dataSetting() {

        Account jjanggu = accountRepository.save(newAccount("kakao_1234", "짱구"));
        Account yuli = accountRepository.save(newAccount("kakao_5678", "유리"));
        Account cheolsu = accountRepository.save(newAccount("kakao_9101", "철수"));
        Account maenggu = accountRepository.save(newAccount("kakao_9999", "맹구"));

        Board jjangguBoard = boardRepository.save(newBoard(jjanggu, "jjangguPost", "jjangguContent", BoardEnum.FREE,
                PetTypeEnum.DOG, false));
        Board yuliBoard = boardRepository.save(newBoard(yuli, "yuliPost", "yuliContent", BoardEnum.FREE,
                PetTypeEnum.RABBIT, false));
        Board cheolsuBoard = boardRepository.save(newBoard(cheolsu, "cheolsuPost", "cheolsuContent", BoardEnum.MEDICAL,
                PetTypeEnum.CAT, true));
        Board maengguBoard = boardRepository.save(newBoard(maenggu, "maengguPost", "maengguContent", BoardEnum.MEDICAL,
                PetTypeEnum.CAT, true));

        likyRepository.save(newLiky(jjangguBoard, yuli.getId(), boardRepository));
        likyRepository.save(newLiky(jjangguBoard, cheolsu.getId(), boardRepository));
        likyRepository.save(newLiky(jjangguBoard, 100L, boardRepository));
        likyRepository.save(newLiky(jjangguBoard, 101L, boardRepository));
        likyRepository.save(newLiky(yuliBoard, jjanggu.getId(), boardRepository));
        likyRepository.save(newLiky(yuliBoard, cheolsu.getId(), boardRepository));
        likyRepository.save(newLiky(cheolsuBoard, jjanggu.getId(), boardRepository));
        likyRepository.save(newLiky(cheolsuBoard, yuli.getId(), boardRepository));
        likyRepository.save(newLiky(cheolsuBoard, 100L, boardRepository));
        likyRepository.save(newLiky(maengguBoard, jjanggu.getId(), boardRepository));
        likyRepository.save(newLiky(maengguBoard, yuli.getId(), boardRepository));

        commentRepository.save(newComment(jjangguBoard, "짱구야 소꿉놀이 하자.", yuli.getId(), boardRepository));
        commentRepository.save(newComment(jjangguBoard, "싫어 나 액션가면 봐야해.", jjanggu.getId(), boardRepository));
        commentRepository.save(newComment(jjangguBoard, "바보", yuli.getId(), boardRepository));
        commentRepository.save(newComment(jjangguBoard, "메롱", jjanggu.getId(), boardRepository));
        commentRepository.save(newComment(jjangguBoard, "짱구야 공부하자.", cheolsu.getId(), boardRepository));
        commentRepository.save(newComment(jjangguBoard, "부리부리 부리부리", jjanggu.getId(), boardRepository));

        commentRepository.save(newComment(yuliBoard, "유리야 그거 재밌어?", jjanggu.getId(), boardRepository));
        commentRepository.save(newComment(yuliBoard, "응 다음에 짱구도 같이 하자.", yuli.getId(), boardRepository));
        commentRepository.save(newComment(yuliBoard, "내일보자 유리야", cheolsu.getId(), boardRepository));
        commentRepository.save(newComment(yuliBoard, "그래", yuli.getId(), boardRepository));

        commentRepository.save(newComment(cheolsuBoard, "철수야 소꿉놀이 하자.", yuli.getId(), boardRepository));
        commentRepository.save(newComment(cheolsuBoard, "안돼 나 학원 가야 할 시간이야.", cheolsu.getId(), boardRepository));
        commentRepository.save(newComment(cheolsuBoard, "철수야 사랑해", jjanggu.getId(), boardRepository));
        commentRepository.save(newComment(cheolsuBoard, "으~ 징그러", cheolsu.getId(), boardRepository));
        commentRepository.save(newComment(cheolsuBoard, "히히 철수도 좋으면서", jjanggu.getId(), boardRepository));
    }

    private void autoIncrementReset() {

        em.createNativeQuery("ALTER TABLE account ALTER COLUMN account_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board ALTER COLUMN board_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE liky ALTER COLUMN liky_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE comment ALTER COLUMN comment_id RESTART WITH 1").executeUpdate();
    }
}