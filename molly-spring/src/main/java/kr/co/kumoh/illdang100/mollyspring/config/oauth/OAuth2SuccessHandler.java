package kr.co.kumoh.illdang100.mollyspring.config.oauth;

import kr.co.kumoh.illdang100.mollyspring.config.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.config.jwt.JwtProcess;
import kr.co.kumoh.illdang100.mollyspring.config.jwt.JwtVO;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.dto.account.AccountRespDto;
import kr.co.kumoh.illdang100.mollyspring.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProcess jwtProcess;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        Account account = principal.getAccount();

        PrincipalDetails loginAccount = (PrincipalDetails) authentication.getPrincipal();
        String jwtToken = jwtProcess.create(loginAccount);
        log.info("jwtToken={}", jwtToken);

        response.addHeader(JwtVO.HEADER, jwtToken);

        AccountRespDto.LoginRespDto loginRespDto = new AccountRespDto.LoginRespDto(account.getId(), account.getUsername());
        CustomResponseUtil.success(response, loginRespDto);
    }
}
