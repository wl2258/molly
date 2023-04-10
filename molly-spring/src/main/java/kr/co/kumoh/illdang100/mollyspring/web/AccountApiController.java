package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;

import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/account")
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/duplicate")
    public ResponseEntity<?> checkNickname(@RequestBody @Valid InputNicknameRequest inputNicknameRequest,
                                           BindingResult bindingResult) {

        String nickname = inputNicknameRequest.getNickname();
        accountService.checkNicknameDuplicate(nickname);

        return new ResponseEntity<>(new ResponseDto<>(1, "사용 가능한 닉네임입니다", null), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> completeRegistration(@ModelAttribute @Valid SaveAccountRequest saveAccountRequest,
                                                  BindingResult bindingResult,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        accountService.saveAdditionalAccountInfo(principalDetails.getAccount().getId(), saveAccountRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "추가정보 기입 완료", null), HttpStatus.OK);
    }
}
