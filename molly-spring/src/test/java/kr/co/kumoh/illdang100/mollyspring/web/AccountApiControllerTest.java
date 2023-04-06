package kr.co.kumoh.illdang100.mollyspring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kumoh.illdang100.mollyspring.config.dummy.DummyObject;
import kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    public void checkNickname_success_test() throws Exception {

        // given
        InputNicknameRequest inputNicknameRequest = new InputNicknameRequest();
        inputNicknameRequest.setNickname("test");

        String requestBody = om.writeValueAsString(inputNicknameRequest);

        // when
        ResultActions resultActions = mvc.perform(post("/api/account/duplicate").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

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
        ResultActions resultActions1 = mvc.perform(post("/api/account/duplicate").content(requestBody1).contentType(MediaType.APPLICATION_JSON));
        ResultActions resultActions2 = mvc.perform(post("/api/account/duplicate").content(requestBody2).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions1.andExpect(status().isBadRequest());
        resultActions2.andExpect(status().isBadRequest());
    }

    private void dataSetting() {

        accountRepository.save(newAccount("molly_1234", "일당백"));
    }
}