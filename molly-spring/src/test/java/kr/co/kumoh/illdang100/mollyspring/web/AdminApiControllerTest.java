package kr.co.kumoh.illdang100.mollyspring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.ComplaintReasonEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.SuspensionDate;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.complaint.BoardComplaintRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.suspension.SuspensionDateRepository;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
import org.assertj.core.api.Assertions;
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

import java.time.LocalDate;

import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private BoardComplaintRepository boardComplaintRepository;
    @Autowired
    private SuspensionDateRepository suspensionDateRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @WithUserDetails(value = "administrator1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void retrieveBoardComplaintList() throws Exception {

        // given

        // when
        // 해당 게시글에 대한 신고날짜가 이미 존재하는 경우 날짜가 제대로 증가되는지 (짱구)
        ResultActions resultActions = mvc.perform(get("/api/admin/board-complaints")
                .param("page", "0")
                .param("size", "3")
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "administrator1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void suspendAccountByBoard() throws Exception {

        // given
        String jjangguEmail = "kakao_1234@naver.com";
        SuspendAccountRequest suspendAccountRequest1 = new SuspendAccountRequest(3L, "ILLEGAL_INFORMATION", jjangguEmail);
        String yuliEmail = "kakao_5678@naver.com";
        SuspendAccountRequest suspendAccountRequest2 = new SuspendAccountRequest(3L, "ILLEGAL_INFORMATION", yuliEmail);
        String cheolsuEmail = "kakao_9101@naver.com";
        SuspendAccountRequest suspendAccountRequest3 = new SuspendAccountRequest(3L, "ILLEGAL_INFORMATION", cheolsuEmail);

        String requestBody1 = om.writeValueAsString(suspendAccountRequest1);
        String requestBody2 = om.writeValueAsString(suspendAccountRequest2);
        String requestBody3 = om.writeValueAsString(suspendAccountRequest3);

        // when
        // 해당 게시글에 대한 신고날짜가 이미 존재하는 경우 날짜가 제대로 증가되는지 (짱구)
        ResultActions resultActions1 = mvc.perform(post("/api/admin/suspend/board/1")
                .content(requestBody1)
                .contentType(MediaType.APPLICATION_JSON));

        // 해당 게시글에 대한 신고날짜가 이미 존재하는데 정지 날짜가 지난 경우 날짜가 제대로 갱신되는지 (유리)
        ResultActions resultActions2 = mvc.perform(post("/api/admin/suspend/board/2")
                .content(requestBody2)
                .contentType(MediaType.APPLICATION_JSON));

        // 해당 게시글에 대한 신고날짜가 존재하지 않은데 제대로 생성되는지 (철수)
        ResultActions resultActions3 = mvc.perform(post("/api/admin/suspend/board/3")
                .content(requestBody3)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions1.andExpect(status().isOk());
        resultActions2.andExpect(status().isOk());
        resultActions3.andExpect(status().isOk());

        SuspensionDate jjangguSuspensionDate = suspensionDateRepository.findByAccountEmail(jjangguEmail).get();
        SuspensionDate yuliSuspensionDate = suspensionDateRepository.findByAccountEmail(yuliEmail).get();
        SuspensionDate cheolsuSuspensionDate = suspensionDateRepository.findByAccountEmail(cheolsuEmail).get();

        Assertions.assertThat(jjangguSuspensionDate.getSuspensionExpiryDate()).isEqualTo(LocalDate.now().plusDays(6));
        Assertions.assertThat(yuliSuspensionDate.getSuspensionExpiryDate()).isEqualTo(LocalDate.now().plusDays(3));
        Assertions.assertThat(cheolsuSuspensionDate.getSuspensionExpiryDate()).isEqualTo(LocalDate.now().plusDays(3));
    }

    @WithUserDetails(value = "administrator1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void suspendAccountByComment() throws Exception {

        // given
        String yuliEmail = "kakao_5678@naver.com";
        SuspendAccountRequest suspendAccountRequest = new SuspendAccountRequest(3L, "ILLEGAL_INFORMATION", yuliEmail);

        String requestBody = om.writeValueAsString(suspendAccountRequest);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/suspend/comment/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    private void dataSetting() {

        Account administrator = accountRepository.save(newAdmin("administrator1", "관리자"));
        Account jjanggu = accountRepository.save(newAccount("kakao_1234", "짱구"));
        Account yuli = accountRepository.save(newAccount("kakao_5678", "유리"));
        Account cheolsu = accountRepository.save(newAccount("kakao_9101", "철수"));

        Board jjangguBoard = boardRepository.save(newBoard(jjanggu, "jjangguPost", "<p>jjangguContent</p>", BoardEnum.FREE,
                PetTypeEnum.DOG, false));
        Board yuliBoard = boardRepository.save(newBoard(yuli, "yuliPost", "<p>yuliContent</p>", BoardEnum.FREE,
                PetTypeEnum.RABBIT, false));
        Board cheolsuBoard = boardRepository.save(newBoard(cheolsu, "cheolsuPost", "<p>cheolsuContent</p>", BoardEnum.MEDICAL,
                PetTypeEnum.CAT, true));

        boardComplaintRepository.save(newBoardComplaint(jjangguBoard, yuli.getEmail(), jjanggu.getEmail(), ComplaintReasonEnum.ANIMAL_CRUELTY));
        boardComplaintRepository.save(newBoardComplaint(yuliBoard, jjanggu.getEmail(), yuli.getEmail(), ComplaintReasonEnum.ANIMAL_CRUELTY));
        boardComplaintRepository.save(newBoardComplaint(cheolsuBoard, jjanggu.getEmail(), yuli.getEmail(), ComplaintReasonEnum.ANIMAL_CRUELTY));

        suspensionDateRepository.save(newSuspensionDate(jjanggu.getEmail(), LocalDate.now().plusDays(3)));
        suspensionDateRepository.save(newSuspensionDate(yuli.getEmail(), LocalDate.now().minusDays(3)));
        commentRepository.save(newComment(jjangguBoard, "짱구야 소꿉놀이 하자.", yuli.getEmail(), boardRepository));

        em.clear();
    }
}