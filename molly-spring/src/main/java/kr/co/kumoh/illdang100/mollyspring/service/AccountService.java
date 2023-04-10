package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.AccountImage;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.image.AccountImageRepository;
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
    private final AccountImageRepository accountImageRepository;
    private final S3Service s3Service;

    /**
     * 닉네임 중복 검사
     * @param nickname 중복 검사를 원하는 닉네임
     */
    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {

        Optional<Account> accountOptional = accountRepository.findByNickname(nickname);

        if (accountOptional.isPresent()) {
            throw new CustomApiException("사용 불가능한 닉네임입니다");
        }
    }

    /**
     * 회원가입 시 닉네임과 프로필 이미지 저장
     * @param accountId 회원가입을 원하는 사용자 pk
     * @param saveAccountRequest 사용자 닉네임과 프로필 이미지 정보가 담긴 request dto
     */
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

            accountImageRepository.save(AccountImage.builder()
                    .account(account)
                    .accountProfileImage(accountImageFile)
                    .build());
        }
    }
}
