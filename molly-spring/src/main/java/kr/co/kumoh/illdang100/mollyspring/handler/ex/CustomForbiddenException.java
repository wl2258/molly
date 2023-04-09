package kr.co.kumoh.illdang100.mollyspring.handler.ex;

import lombok.Getter;

@Getter
public class CustomForbiddenException extends RuntimeException{

    public CustomForbiddenException(String message) {
        super(message);
    }
}
