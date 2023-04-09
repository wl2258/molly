package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {

        Optional<Account> accountOptional = accountRepository.findByNickname(nickname);

        if (accountOptional.isPresent()) {
            throw new CustomApiException("사용 불가능한 닉네임입니다");
        }
    }

    @Transactional
    public void saveAdditionalAccountInfo(Long accountId, SaveAccountRequest saveAccountRequest) throws IOException {

        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));

        String nickname = saveAccountRequest.getNickname();

        checkNicknameDuplicate(nickname);
        account.changeNickname(nickname);

        if (saveAccountRequest.getAccountProfileImage() != null) {

            ImageFile accountImageFile =
                    s3Service.upload(saveAccountRequest.getAccountProfileImage(), FileRootPathVO.ACCOUNT_PATH);
            account.changeProfileImage(accountImageFile);
        }
    }
}
