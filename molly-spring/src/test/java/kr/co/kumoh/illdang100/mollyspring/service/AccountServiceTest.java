package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.config.dummy.DummyObject;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void checkNicknameDuplicate_test() {

        // given
        Account account = newAccount("molly_1234", "일당백");

        // stub
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.empty());

        // when

        // then
        accountService.checkNicknameDuplicate(account.getNickname());
        verify(accountRepository, times(1)).findByNickname(account.getNickname());
    }

    @Test
    public void checkNicknameDuplicate_exception_test() {

        // given
        Account account = newAccount("molly_1234", "일당백");

        // stub
        when(accountRepository.findByNickname(account.getNickname())).thenReturn(Optional.of(account));

        // when

        // then
        Assertions.assertThatThrownBy(() -> accountService.checkNicknameDuplicate(account.getNickname()))
                .isInstanceOf(CustomApiException.class);
    }
}