package kr.co.kumoh.illdang100.mollyspring.config.dummy;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

    protected Account newAccount(String username, String nickname) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return Account.builder()
                .username(username)
                .password(encPassword)
                .email(username + "@naver.com")
                .nickname(nickname)
                .role(AccountEnum.CUSTOMER)
                .build();
    }

    protected Account newMockAccount(Long id, String username, String nickname, AccountEnum accountEnum) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return Account.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username + "@naver.com")
                .nickname(nickname)
                .role(accountEnum)
                .build();
    }
}
