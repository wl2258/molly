package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.service.AccountService;
import kr.co.kumoh.illdang100.mollyspring.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/account/duplicate")
    public ResponseEntity<?> checkNickname(@RequestBody @Valid InputNicknameRequest inputNicknameRequest,
                                           BindingResult bindingResult) {

        String nickname = inputNicknameRequest.getNickname();
        accountService.checkNicknameDuplicate(nickname);

        return new ResponseEntity<>(new ResponseDto<>(1, "사용 가능한 닉네임입니다.", null), HttpStatus.OK);
    }

    @PostMapping("/account/{accountId}")
    public void completeRegistration(@PathVariable Long accountId,
                                                  @RequestBody @Valid SaveAccountRequest saveAccountRequest,
                                                  BindingResult bindingResult,
                                                  HttpServletResponse response) throws IOException {

        accountService.saveAdditionalAccountInfo(accountId, saveAccountRequest);

        CustomResponseUtil.redirect(response, "http://localhost:3000/");
    }
}
