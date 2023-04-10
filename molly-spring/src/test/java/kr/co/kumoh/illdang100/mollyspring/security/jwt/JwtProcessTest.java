package kr.co.kumoh.illdang100.mollyspring.security.jwt;

import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
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

        // given
        Account account1 = newMockAccount(1L, "google_1234", "일당백", AccountEnum.CUSTOMER);
        Account account2 = newMockAccount(2L, "kakao_1234", "몰리", AccountEnum.ADMIN);

        String accessToken1 = jwtProcess.createNewAccessToken(account1.getId(), account1.getRole().toString());
        String accessToken2 = jwtProcess.createNewAccessToken(account2.getId(), account2.getRole().toString());

        String jwtToken1 = accessToken1.replace(JwtVO.TOKEN_PREFIX, "");
        String jwtToken2 = accessToken2.replace(JwtVO.TOKEN_PREFIX, "");

        // when
        PrincipalDetails createdPrincipalDetails1 = jwtProcess.verify(jwtToken1);
        PrincipalDetails createdPrincipalDetails2 = jwtProcess.verify(jwtToken2);
        Account createdAccount1 = createdPrincipalDetails1.getAccount();
        Account createdAccount2 = createdPrincipalDetails2.getAccount();

        // then
        assertThat(createdAccount1.getId()).isEqualTo(1L);
        assertThat(createdAccount2.getId()).isEqualTo(2L);

        assertThat(createdAccount1.getRole()).isEqualTo(AccountEnum.CUSTOMER);
        assertThat(createdAccount2.getRole()).isEqualTo(AccountEnum.ADMIN);
    }
}