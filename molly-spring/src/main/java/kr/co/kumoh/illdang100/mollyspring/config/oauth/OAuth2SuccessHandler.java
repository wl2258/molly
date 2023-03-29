package kr.co.kumoh.illdang100.mollyspring.config.oauth;

import kr.co.kumoh.illdang100.mollyspring.config.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.config.jwt.JwtProcess;
import kr.co.kumoh.illdang100.mollyspring.config.jwt.JwtVO;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.dto.account.AccountRespDto;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

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
        String jwtToken = jwtProcess.create(principal);
        response.addHeader(JwtVO.HEADER, jwtToken);

        // TODO: 인증에 성공하면 접근 실패한 uri를 http://localhost:3000/ 뒤에 추가해서 리다이렉트 시키기!!
        String additionalInputUri = "";

        if (account.getNickname() == null) {
            additionalInputUri = "home/signup";
        }

        String redirectUrl = makeRedirectUrl(additionalInputUri);
        log.info("redirectUrl={}", redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String makeRedirectUrl(String uri) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/" + uri).build().toUriString();
    }
}
