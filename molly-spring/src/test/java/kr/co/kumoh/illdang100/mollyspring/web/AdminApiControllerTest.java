package kr.co.kumoh.illdang100.mollyspring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class AdminApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @WithUserDetails(value = "administrator1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void suspendAccountByBoard() throws Exception {

        // given
        SuspendAccountRequest suspendAccountRequest = new SuspendAccountRequest(3L, "ILLEGAL_INFORMATION");

        String requestBody = om.writeValueAsString(suspendAccountRequest);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/account/2/suspend/board/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "administrator1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void suspendAccountByComment() throws Exception {

        // given
        SuspendAccountRequest suspendAccountRequest = new SuspendAccountRequest(3L, "ILLEGAL_INFORMATION");

        String requestBody = om.writeValueAsString(suspendAccountRequest);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/account/2/suspend/comment/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    private void dataSetting() {

        Account administrator = accountRepository.save(newAdmin("administrator1", "관리자"));
        Account jjanggu = accountRepository.save(newAccount("kakao_1234", "짱구"));
        Board jjangguBoard = boardRepository.save(newBoard(jjanggu, "jjangguPost", "<p>jjangguContent</p>",
                BoardEnum.FREE, PetTypeEnum.DOG, false));
        commentRepository.save(newComment(jjangguBoard, "짱구야 소꿉놀이 하자.", administrator.getEmail(), boardRepository));
        em.clear();
    }
}