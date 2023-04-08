package kr.co.kumoh.illdang100.mollyspring.config.jwt;

import kr.co.kumoh.illdang100.mollyspring.config.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.config.dummy.DummyObject;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtProcessTest extends DummyObject {

    @Autowired
    private JwtProcess jwtProcess;

    @Test
    public void createAccessToken_test() throws Exception {

        // given
        Account account1 = newMockAccount(1L, "google_1234", "일당백", AccountEnum.CUSTOMER);
        Account account2 = newMockAccount(2L, "kakao_1234", "몰리", AccountEnum.ADMIN);
        PrincipalDetails principalDetails1 = new PrincipalDetails(account1);
        PrincipalDetails principalDetails2 = new PrincipalDetails(account2);

        // when
        String jwtToken1 = jwtProcess.createAccessToken(principalDetails1);
        String jwtToken2 = jwtProcess.createAccessToken(principalDetails2);
        System.out.println("jwtToken1 = " + jwtToken1);
        System.out.println("jwtToken2 = " + jwtToken2);

        // then
        assertThat(jwtToken1.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        assertThat(jwtToken2.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    public void createRefreshToken_test() throws Exception {

        // given

        // when
        String refreshToken1 = jwtProcess.createRefreshToken("1", "CUSTOMER");
        String refreshToken2 = jwtProcess.createRefreshToken("2", "ADMIN");
        System.out.println("refreshToken1 = " + refreshToken1);
        System.out.println("refreshToken2 = " + refreshToken2);

        // then
        assertThat(refreshToken1.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        assertThat(refreshToken2.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    public void verify_test() throws Exception {

        // 테스트에 사용되는 토큰에 만료기한이 있어 토큰 임시로 발급받고 테스트하기
        /*// given
        String jwtToken1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLssL3snZjshKTqs4TtlITroZzsoJ3tirjsnbzri7nrsLHrqrDrpqwiLCJyb2xlIjoiQ1VTVE9NRVIiLCJpZCI6MSwiZXhwIjoxNjgwODY3Mjc1fQ.btwJHo3cfi9xrwGzpxJyBQVDYKLiJUmmF2jlm1aJLTqJxpfXCSFwC8JjpIzbyVzJBJu-u2qBEnzJmyitSEztig";
        String jwtToken2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLssL3snZjshKTqs4TtlITroZzsoJ3tirjsnbzri7nrsLHrqrDrpqwiLCJyb2xlIjoiQURNSU4iLCJpZCI6MiwiZXhwIjoxNjgwODY3Mjc1fQ.hh4ouZaXSWvSE5qa6UdSG2JYqbrVuskp2XOzZMuLi_iAFGy-ItCWXv2XlwgIylIy3ehZMSSQ7u2kfczmhTUSFw";

        // when
        PrincipalDetails principalDetails1 = jwtProcess.verify(jwtToken1);
        PrincipalDetails principalDetails2 = jwtProcess.verify(jwtToken2);
        Account account1 = principalDetails1.getAccount();
        Account account2 = principalDetails2.getAccount();

        // then
        assertThat(account1.getId()).isEqualTo(1L);
        assertThat(account2.getId()).isEqualTo(2L);

        assertThat(account1.getRole()).isEqualTo(AccountEnum.CUSTOMER);
        assertThat(account2.getRole()).isEqualTo(AccountEnum.ADMIN);*/
    }
}