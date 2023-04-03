package kr.co.kumoh.illdang100.mollyspring.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.co.kumoh.illdang100.mollyspring.config.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProcess {

    @Value("${jwt.subject}")
    private String jwtSubject;

    @Value("${jwt.secret}")
    private String secret;

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 토큰 생성
    public String create(PrincipalDetails principalDetails) {

        Account account = principalDetails.getAccount();

        String jwtToken = JWT.create()
                .withSubject(jwtSubject)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                .withClaim("id", account.getId())
                .withClaim("role", account.getRole() + "")
                .sign(Algorithm.HMAC512(secret));

        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정) - 강제 로그인
    public PrincipalDetails verify(String token) {

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secret)).build().verify(token);

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        Account account = Account.builder().id(id).role(AccountEnum.valueOf(role)).build();

        return new PrincipalDetails(account);
    }
}
