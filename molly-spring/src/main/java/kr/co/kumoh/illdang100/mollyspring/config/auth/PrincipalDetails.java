package kr.co.kumoh.illdang100.mollyspring.config.auth;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


public class PrincipalDetails implements OAuth2User {

    private Account account;

    private Map<String, Object> attributes;

    public PrincipalDetails(Account account) {
        this.account = account;
    }

    public PrincipalDetails(Account account, Map<String, Object> attributes) {
        this.account = account;
        this.attributes = attributes;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(() -> "ROLE_" + account.getRole());

        return authorities;
    }

    @Override
    public String getName() {
        return account.getId() + "";
    }
}
