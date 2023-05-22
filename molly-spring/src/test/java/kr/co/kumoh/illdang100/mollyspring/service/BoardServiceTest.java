package kr.co.kumoh.illdang100.mollyspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.liky.LikyRepository;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest extends DummyObject {

    @InjectMocks
    private BoardService boardService;


    @Mock
    private AccountService accountService;

    @Mock
    private S3Service s3Service;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private LikyRepository likyRepository;

    @Spy
    private ObjectMapper om;


    @Test
    public void createPost_test() {

        // given


        // when


        // then
    }

    @Test
    public void updatePost_test() {

        // given


        // when


        // then
    }

    @Test
    public void deletePost_test() {

        // given


        // when


        // then
    }

    @Test
    public void getPostDetail_test() {

        // given


        // when


        // then
    }

    @Test
    public void toggleLikePost_create_test() {

        // given
        Long accountId = 1L;
        Long boardId = 1L;

        // stub
        Account account = newMockAccount(accountId, "kakao_1234", "molly", AccountEnum.CUSTOMER);
        Board board =
                newMockBoard(boardId, account, "testTitle", "testContent", BoardEnum.FREE, PetTypeEnum.DOG, false);
        when(boardRepository.findWithAccountById(any())).thenReturn(Optional.of(board));

        // stub
        Liky liky = newMockLiky(1L, board, accountId, boardRepository);
        when(likyRepository.existsByAccountIdAndBoard_Id(any(), any())).thenReturn(false);

        // when
        LikyBoardResponse likyBoardResponse = boardService.toggleLikePost(accountId, boardId);

        // then
        assertThat(likyBoardResponse.getLikyCount()).isEqualTo(12);
        assertThat(likyBoardResponse.getMessage()).isEqualTo("게시글 좋아요");
        assertThat(board.getLikyCnt()).isEqualTo(12);
    }

    @Test
    public void toggleLikePost_delete_test() {

        // given
        Long accountId = 1L;
        Long boardId = 1L;

        // stub
        Account account = newMockAccount(accountId, "kakao_1234", "molly", AccountEnum.CUSTOMER);
        Board board =
                newMockBoard(boardId, account, "testTitle", "testContent", BoardEnum.FREE, PetTypeEnum.DOG, false);
        when(boardRepository.findWithAccountById(any())).thenReturn(Optional.of(board));

        // stub
        // newMockLiky 이후 board의 likyCnt값 감소하는지 검사하기
        when(likyRepository.existsByAccountIdAndBoard_Id(any(), any())).thenReturn(true);

        // when
        LikyBoardResponse likyBoardResponse = boardService.toggleLikePost(accountId, boardId);

        // then
        assertThat(likyBoardResponse.getLikyCount()).isEqualTo(9);
        assertThat(likyBoardResponse.getMessage()).isEqualTo("게시글 좋아요 삭제");
        assertThat(board.getLikyCnt()).isEqualTo(9);
    }


}