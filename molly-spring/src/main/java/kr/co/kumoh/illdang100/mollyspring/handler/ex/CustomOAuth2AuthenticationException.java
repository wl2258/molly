package kr.co.kumoh.illdang100.mollyspring.handler.ex;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class CustomOAuth2AuthenticationException extends OAuth2AuthenticationException {

    private String errorMessage;

    public CustomOAuth2AuthenticationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
