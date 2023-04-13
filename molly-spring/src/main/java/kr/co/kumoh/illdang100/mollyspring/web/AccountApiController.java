package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.account.AccountRespDto;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.security.jwt.JwtVO;
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
import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/auth/account/duplicate")
    public ResponseEntity<?> checkNickname(@RequestBody @Valid InputNicknameRequest inputNicknameRequest,
                                           BindingResult bindingResult) {

        String nickname = inputNicknameRequest.getNickname();
        accountService.checkNicknameDuplicate(nickname);

        return new ResponseEntity<>(new ResponseDto<>(1, "사용 가능한 닉네임입니다", null), HttpStatus.OK);
    }

    @PostMapping("/auth/account/save")
    public ResponseEntity<?> completeRegistration(@ModelAttribute @Valid SaveAccountRequest saveAccountRequest,
                                                  BindingResult bindingResult,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        accountService.saveAdditionalAccountInfo(principalDetails.getAccount().getId(), saveAccountRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "추가정보 기입 완료", null), HttpStatus.OK);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<?> getAccountProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        AccountProfileResponse accountProfileResponse =
                accountService.getAccountDetail(principalDetails.getAccount().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보 조회 성공", accountProfileResponse), HttpStatus.OK);
    }

    // TODO: 사용자 정보 (닉네임) 수정
    @PatchMapping("/auth/account/nickname")
    public ResponseEntity<?> updateAccountProfile(@RequestBody @Valid InputNicknameRequest inputNicknameRequest,
                                                  BindingResult bindingResult,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {

        accountService.updateAccountNickname(principalDetails.getAccount().getId(), inputNicknameRequest.getNickname());

        return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/account/logout")
    public ResponseEntity<?> logout(@RequestHeader(JwtVO.REFRESH_TOKEN_HEADER) String refreshToken) {

        accountService.deleteRefreshToken(refreshToken);

        return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 성공", null), HttpStatus.OK);
    }

    // TODO: 사용자 프로필 이미지 변경 및 수정
}
