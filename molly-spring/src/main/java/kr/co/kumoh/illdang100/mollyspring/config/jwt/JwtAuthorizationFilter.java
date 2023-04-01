package kr.co.kumoh.illdang100.mollyspring.config.jwt;

import kr.co.kumoh.illdang100.mollyspring.config.auth.PrincipalDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 모든 주소에서 동작한다. (토큰 검증)
 * 예외가 발생하면 SecurityConfig 파일의 권한 실패 예외 발생
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProcess jwtProcess;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 토큰이 존재하는지 검사한다.
        if (isHeaderVerify(request, response)) {

            String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
            PrincipalDetails loginUser = jwtProcess.verify(token);

            // 임시 세션
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            loginUser,
                            null,
                            loginUser.getAuthorities());

            // 강제 로그인이 진행된다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVO.HEADER);
        if (header == null || !header.startsWith(JwtVO.TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }
}
