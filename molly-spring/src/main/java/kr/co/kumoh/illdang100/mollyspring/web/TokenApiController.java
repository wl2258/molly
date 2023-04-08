package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.config.jwt.JwtVO;
import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenApiController {

    private final AccountService accountService;

    @PostMapping("/refresh")
    public ResponseEntity<?> reIssueAccessToken(@RequestHeader(JwtVO.REFRESH_TOKEN_HEADER) String refreshToken,
                                                HttpServletResponse response) {

        accountService.reIssueToken(response, refreshToken);

        return new ResponseEntity<>(new ResponseDto<>(1, "토큰 재발급에 성공하였습니다", null), HttpStatus.OK);
    }
}
