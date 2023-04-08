package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.config.jwt.JwtProcess;
import kr.co.kumoh.illdang100.mollyspring.config.jwt.JwtVO;
import kr.co.kumoh.illdang100.mollyspring.config.jwt.RefreshToken;
import kr.co.kumoh.illdang100.mollyspring.config.jwt.RefreshTokenRedisRepository;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    private final JwtProcess jwtProcess;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional(readOnly = true)
    public ResponseDto checkNicknameDuplicate(String nickname) {

        Optional<Account> accountOptional = accountRepository.findByNickname(nickname);

        if (accountOptional.isPresent()) {
            throw new CustomApiException("사용 불가능한 닉네임입니다");
        }

        return new ResponseDto<>(1, "사용 가능한 닉네임입니다", null);
    }

    @Transactional
    public ResponseDto saveAdditionalAccountInfo(Long accountId, SaveAccountRequest saveAccountRequest) throws IOException {

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

        return new ResponseDto<>(1, "추가정보 기입 완료", null);
    }

    @Transactional
    public void reIssueToken(HttpServletResponse response, String refreshToken) {

        // 1. Request Header에서 JWT Token 추출
        String extractedToken = extractToken(refreshToken);

        // refresh token 유효성 검사
        try {
            jwtProcess.isSatisfiedToken(extractedToken);
        } catch (Exception e) {
            throw new CustomApiException("유효하지 않은 토큰입니다");
        }

        // 2. 저장된 refresh token 찾기
        RefreshToken findRefreshToken = refreshTokenRedisRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomApiException("토큰 갱신에 실패했습니다"));

        // 3. 저장된 refresh token 정보를 바탕으로 새로운 JWT Token 생성
        String accountId = findRefreshToken.getId();
        String accountRole = findRefreshToken.getRole();

        String reAccessToken =
                jwtProcess.createNewAccessToken(Long.valueOf(accountId), accountRole);

        // 새로운 refresh token을 발급함으로써 refresh token이 탈취되어도 아무런 문제가 없게 된다.
        String reRefreshToken =
                jwtProcess.createRefreshToken(accountId, accountRole);

        findRefreshToken.changeRefreshToken(reRefreshToken);
        refreshTokenRedisRepository.save(findRefreshToken);

        response.addHeader(JwtVO.ACCESS_TOKEN_HEADER, reAccessToken);
        response.addHeader(JwtVO.REFRESH_TOKEN_HEADER, reRefreshToken);
    }

    private static String extractToken(String refreshToken) {
        return refreshToken.replace(JwtVO.TOKEN_PREFIX, "");
    }
}
