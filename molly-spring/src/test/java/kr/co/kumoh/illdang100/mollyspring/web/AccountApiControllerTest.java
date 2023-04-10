package kr.co.kumoh.illdang100.mollyspring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class AccountApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @WithUserDetails(value = "molly_1234", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void checkNickname_success_test() throws Exception {

        // given
        InputNicknameRequest inputNicknameRequest = new InputNicknameRequest();
        inputNicknameRequest.setNickname("test");

        String requestBody = om.writeValueAsString(inputNicknameRequest);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/account/duplicate")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "molly_1234", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void checkNickname_failure_test() throws Exception {

        // given
        InputNicknameRequest inputNicknameRequest1 = new InputNicknameRequest();
        inputNicknameRequest1.setNickname("testNickname");

        InputNicknameRequest inputNicknameRequest2 = new InputNicknameRequest();
        inputNicknameRequest2.setNickname("일당백");

        String requestBody1 = om.writeValueAsString(inputNicknameRequest1);
        String requestBody2 = om.writeValueAsString(inputNicknameRequest2);

        // when
        ResultActions resultActions1 = mvc.perform(post("/api/auth/account/duplicate")
                .content(requestBody1)
                .contentType(MediaType.APPLICATION_JSON));
        ResultActions resultActions2 = mvc.perform(post("/api/auth/account/duplicate")
                .content(requestBody2)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions1.andExpect(status().isBadRequest());
        resultActions2.andExpect(status().isBadRequest());
    }

    @WithUserDetails(value = "molly_1234", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void completeRegistration_success_test() throws Exception {

        // given
        String nickname = "test";

        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/account/save")
                                .param("nickname", nickname)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "molly_1234", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void completeRegistration_failure_test() throws Exception {

        // given
        String nickname1 = "testNickname";
        String nickname2 = "";

        // when
        // 1. nickname 범위가 10을 넘어가는 경우
        ResultActions resultActions1 =
                mvc.perform(post("/api/auth/account/save")
                        .param("nickname", nickname1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));


        // 2. nickname 값이 빈 문자열인 경우
        ResultActions resultActions2 =
                mvc.perform(post("/api/auth/account/save")
                        .param("nickname", nickname2)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));


        // 3. nickname 값이 null인 경우
        ResultActions resultActions3 =
                mvc.perform(post("/api/auth/account/save")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        String responseBody1 = resultActions1.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody1 = " + responseBody1);

        String responseBody2 = resultActions2.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody2 = " + responseBody2);

        String responseBody3 = resultActions3.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody3 = " + responseBody3);

        // then
        resultActions1.andExpect(status().isBadRequest());
        resultActions2.andExpect(status().isBadRequest());
        resultActions3.andExpect(status().isBadRequest());
    }

    private void dataSetting() {

        accountRepository.save(newAccount("molly_1234", "일당백"));
        em.clear();
    }
}