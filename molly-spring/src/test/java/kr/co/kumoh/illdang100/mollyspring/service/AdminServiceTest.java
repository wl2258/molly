package kr.co.kumoh.illdang100.mollyspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.SuspensionDate;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.suspension.SuspensionDateRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.suspension.SuspensionRepository;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AdminServiceTest extends DummyObject {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private SuspensionRepository suspensionRepository;
    @Mock
    private SuspensionDateRepository suspensionDateRepository;
    @Mock
    private BoardRepository boardRepository;

    @Test
    public void suspendAccount_success_test() {

        // given
        Long accountId = 1L;
        Long boardId = 1L;
        Long commentId = null;
        SuspendAccountRequest suspendAccountRequest = new SuspendAccountRequest(3L, "SPAM_PROMOTION");

        // stub
        Account account = newMockAccount(accountId, "username", "nickname", AccountEnum.CUSTOMER);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // stub
        when(suspensionRepository.existsByBoardId(boardId)).thenReturn(false);

        // stub
        Board board = newMockBoard(boardId, account, "title", "content", BoardEnum.MEDICAL, PetTypeEnum.DOG, false);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // stub
        SuspensionDate suspensionDate = newMockSuspensionDate(1L, account.getEmail(), LocalDate.now());
        when(suspensionDateRepository.findByAccountEmail(account.getEmail())).thenReturn(Optional.of(suspensionDate));

        // when
        adminService.suspendAccount(accountId, boardId, commentId, suspendAccountRequest);

        // then
        assertThat(suspensionDate.getSuspensionExpiryDate()).isEqualTo(LocalDate.now().plusDays(3));
    }

    @Test
    public void suspendAccount_failure_test() {

        // given
        Long accountId = 1L;
        Long boardId = 1L;
        Long commentId = null;
        SuspendAccountRequest suspendAccountRequest = new SuspendAccountRequest(3L, "SPAM_PROMOTION");

        // stub
        Account account = newMockAccount(accountId, "username", "nickname", AccountEnum.CUSTOMER);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // stub
        when(suspensionRepository.existsByBoardId(boardId)).thenReturn(false);

        // stub
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> adminService.suspendAccount(accountId, boardId, commentId, suspendAccountRequest))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("존재하지 않는 게시글입니다");
    }
}