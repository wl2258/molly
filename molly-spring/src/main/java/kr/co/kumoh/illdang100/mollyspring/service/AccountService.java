package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.Pet;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.pet.PetRepository;
import kr.co.kumoh.illdang100.mollyspring.security.jwt.RefreshToken;
import kr.co.kumoh.illdang100.mollyspring.security.jwt.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.account.AccountRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final S3Service s3Service;
    private final PetService petService;
    private final PetRepository petRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    /**
     * 닉네임 중복 검사
     *
     * @param nickname 중복 검사를 원하는 닉네임
     */
    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {

        Optional<Account> accountOpt = accountRepository.findByNickname(nickname);

        accountOpt.ifPresent((findAccount) -> {
            throw new CustomApiException("사용 불가능한 닉네임입니다.");
        });
    }

    /**
     * 회원가입 시 닉네임과 프로필 이미지 저장
     *
     * @param accountId          회원가입을 원하는 사용자 pk
     * @param saveAccountRequest 사용자 닉네임과 프로필 이미지 정보가 담긴 request dto
     */
    @Transactional
    public void saveAdditionalAccountInfo(Long accountId, SaveAccountRequest saveAccountRequest) {

        Account account = findAccountByIdOrThrowException(accountId);

        String nickname = saveAccountRequest.getNickname();

        checkNicknameDuplicate(nickname);
        account.changeNickname(nickname);

        if (saveAccountRequest.getAccountProfileImage() != null) {
            try {
                ImageFile accountImageFile =
                        s3Service.upload(saveAccountRequest.getAccountProfileImage(), FileRootPathVO.ACCOUNT_PATH);
                account.changeProfileImage(accountImageFile);
            } catch (Exception e) {
                throw new CustomApiException(e.getMessage());
            }
        }
    }

    private Account findAccountByIdOrThrowException(Long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));
    }

    /**
     * 사용자 정보 조회
     *
     * @param accountId 사용자 pk
     * @return 사용자 프로필 이미지, 닉네임, 이메일, 회원가입 경로
     */
    public AccountProfileResponse getAccountDetail(Long accountId) {

        Account account = findAccountByIdOrThrowException(accountId);

        AccountProfileResponse accountProfileResponse = new AccountProfileResponse(account);

        if (hasAccountProfileImage(account)) {
            accountProfileResponse.setProfileImage(account.getAccountProfileImage().getStoreFileUrl());
        }

        return accountProfileResponse;
    }

    @Transactional
    public void updateAccountNickname(Long accountId, String nickname) {

        Account account = findAccountByIdOrThrowException(accountId);

        checkNicknameDuplicate(nickname);
        account.changeNickname(nickname);
    }

    /**
     * 사용자 로그아웃 - 리프래시 토큰 삭제
     *
     * @param refreshToken 리프래시 토큰
     */
    @Transactional
    public void deleteRefreshToken(String refreshToken) {

        Optional<RefreshToken> refreshTokenOpt = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

        if (refreshTokenOpt.isPresent()) {
            log.debug("Refresh Token 삭제");
            RefreshToken findRefreshToken = refreshTokenOpt.get();
            refreshTokenRedisRepository.delete(findRefreshToken);
        }
    }

    /**
     * 사용자 프로필 이미지 변경
     *
     * @param accountProfileImage 변경하고자 하는 사용자 이미지
     */
    @Transactional
    public void updateAccountProfileImage(Long accountId, MultipartFile accountProfileImage) {

        Account account = findAccountByIdOrThrowException(accountId);

        try {
            ImageFile accountImageFile = s3Service.upload(accountProfileImage, FileRootPathVO.ACCOUNT_PATH);

            if (hasAccountProfileImage(account))
                s3Service.delete(account.getAccountProfileImage().getStoreFileName());

            account.changeProfileImage(accountImageFile);

        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }

    private static boolean hasAccountProfileImage(Account account) {
        return account.getAccountProfileImage() != null;
    }

    /**
     * 회원 탈퇴
     *
     * @param accountId
     */
    @Transactional
    public void deleteAccount(Long accountId) {

        Account findAccount = findAccountByIdOrThrowException(accountId);

        // s3 프로필 이미지 삭제
        deleteAccountProfileImage(findAccount);

        // pet 삭제
        deletePetByAccountId(accountId);

        // board 삭제
        deleteBoardByAccountId(accountId);

        accountRepository.delete(findAccount);
    }

    private void deleteBoardByAccountId(Long accountId) {
        List<Board> findBoards = boardRepository.findByAccount_Id(accountId);
        findBoards.forEach(board -> {
            boardService.deletePost(board.getId(), accountId);
        });
    }

    private void deletePetByAccountId(Long accountId) {
        List<Pet> findPets = petRepository.findByAccount_Id(accountId);
        findPets.forEach(pet -> {
            petService.deletePet(pet.getId());
        });
    }

    private void deleteAccountProfileImage(Account findAccount) {
        if (hasAccountProfileImage(findAccount)) {
            s3Service.delete(findAccount.getAccountProfileImage().getStoreFileName());
        }
    }

    /**
     * 사용자 프로필 이미지 삭제 (기본 이미지 변경)
     *
     * @param accountId 사용자 pk
     */
    @Transactional
    public void deleteAccountProfileImage(Long accountId) {

        Account findAccount = findAccountByIdOrThrowException(accountId);

        if (hasAccountProfileImage(findAccount)) {
            s3Service.delete(findAccount.getAccountProfileImage().getStoreFileName());
            findAccount.changeProfileImage(null);
        }
    }
}
