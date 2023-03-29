package kr.co.kumoh.illdang100.mollyspring.web.acccount;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountApiController {

    private final AccountRepository accountRepository;

    @PostMapping("/account/duplicate")
    public ResponseEntity<?> duplicateNickname(@RequestBody @Valid InputNicknameReqDto inputNicknameReqDto, BindingResult bindingResult) {

        String nickname = inputNicknameReqDto.getNickname();
        Optional<Account> accountOptional = accountRepository.findByNickname(nickname);

        if (accountOptional.isPresent()) {
            throw new CustomApiException("사용 불가능한 닉네임입니다.");
        }

        return new ResponseEntity<>(new ResponseDto<>(1, "사용 가능한 닉네임입니다.", null), HttpStatus.OK);
    }
}
