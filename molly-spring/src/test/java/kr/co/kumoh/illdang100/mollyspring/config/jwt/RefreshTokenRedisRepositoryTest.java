package kr.co.kumoh.illdang100.mollyspring.config.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class RefreshTokenRedisRepositoryTest {

    @Autowired
    RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Test
    public void test() {

        RefreshToken refreshToken = RefreshToken.builder()
                .id("testId")
                .role("CUSTOMER")
                .refreshToken("testToken")
                .build();

        refreshTokenRedisRepository.save(refreshToken);

        Optional<RefreshToken> testToken = refreshTokenRedisRepository.findByRefreshToken("testToken");

        RefreshToken refreshToken1 = testToken.get();

        System.out.println("refreshToken1.getId() = " + refreshToken1.getId());
        System.out.println("refreshToken1.getRole() = " + refreshToken1.getRole());
        System.out.println("refreshToken1.getRefreshToken() = " + refreshToken1.getRefreshToken());
    }
}