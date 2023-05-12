package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.image.AccountImage;
import kr.co.kumoh.illdang100.mollyspring.domain.image.BoardImage;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.image.AccountImageRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.image.BoardImageRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.liky.LikyRepository;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.security.jwt.JwtProcess;
import kr.co.kumoh.illdang100.mollyspring.security.jwt.JwtVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final S3Service s3Service;
    private final BoardImageRepository boardImageRepository;
    private final AccountImageRepository accountImageRepository;
    private final JwtProcess jwtProcess;
    private final CommentRepository commentRepository;
    private final LikyRepository likyRepository;

    @Transactional
    public CreatePostResponse createPost(Long accountId, CreatePostRequest createPostRequest) {

        Account findAccount = findAccountByIdOrThrowException(accountId);

        Board board = boardRepository.save(new Board(findAccount, createPostRequest));

        // 게시글 이미지 저장
        List<MultipartFile> boardImages = createPostRequest.getBoardImages();
        if (boardImages != null && !boardImages.isEmpty()) {
            for (MultipartFile boardImage : boardImages) {
                try {
                    saveBoardImage(board, boardImage);
                    board.changeHasImage(true);
                } catch (Exception e) {
                    throw new CustomApiException(e.getMessage());
                }
            }
        }

        return new CreatePostResponse(board.getId());
    }

    public Page<RetrievePostListDto> getPostList(RetrievePostListCondition retrievePostListCondition, Pageable pageable) {

        return boardRepository.findPagePostList(retrievePostListCondition, pageable);
    }

    // TODO: 게시글 상세조회
    @Transactional
    public PostDetailResponse getPostDetail(Long boardId, String jwtToken) {

        Board board = boardRepository.findWithAccountById(boardId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 게시글입니다"));

        board.increaseViews();

        // TODO: BoardImage 조회 순서 정하기!!
        List<String> boardImages = boardImageRepository.findByBoard_id(boardId)
                .stream()
                .map(boardImage -> boardImage.getAccountProfileImage().getStoreFileUrl())
                .collect(Collectors.toList());

        boolean isOwner = false;
        boolean thumbsUp = false;
        Long accountId = null;

        if (jwtToken != null) {
            String token = jwtToken.replace(JwtVO.TOKEN_PREFIX, "");
            PrincipalDetails principalDetails = jwtProcess.verify(token);
            accountId = principalDetails.getAccount().getId();
        }

        if (board.getAccount().getId().equals(accountId)) {
            isOwner = true;
        } else if (accountId != null) {
            thumbsUp = likyRepository.existsByAccountIdAndBoard_Id(accountId, boardId);
        }

        String writerProfileImage = accountImageRepository.findByAccount_id(board.getAccount().getId())
                .map(accountImage -> accountImage.getAccountProfileImage().getStoreFileUrl()).orElse(null);

        List<Comment> comments = commentRepository.findByBoard_IdOrderByCreatedDate(boardId);
        List<BoardCommentDto> commentDtoList = comments.stream()
                .map(comment -> new BoardCommentDto(
                        comment.getAccountId(),
                        comment.getWriterNickname(),
                        comment.getCreatedDate(),
                        comment.getCommentContent(),
                        comment.getWriterProfileUrl()))
                .collect(Collectors.toList());

        // TODO: 해당 사용자 ID 리스트로 사용자 프로필 이미지 조회해오기
        List<Long> commentAccountIds = comments.stream().map(Comment::getAccountId).collect(Collectors.toList());

        return PostDetailResponse.builder()
                .isOwner(isOwner)
                .title(board.getBoardTitle())
                .boardImages(boardImages)
                .content(board.getBoardContent())
                .writerNick(board.getAccount().getNickname())
                .createdAt(board.getCreatedDate())
                .views(board.getViews())
                .writerProfileImage(writerProfileImage)
                .comments(commentDtoList)
                .thumbsUp(thumbsUp)
                .likyCnt(board.getLikyCnt())
                .build();
    }

    // TODO: 좋아요
    @Transactional
    public void toggleLikePost() {

    }

    // TODO: 게시글 수정
    @Transactional
    public void updatePost() {

    }

    // TODO: 게시글 삭제
    @Transactional
    public void deletePost() {

    }

    private void saveBoardImage(Board board, MultipartFile boardImage) throws IOException {
        ImageFile boardImageFile =
                s3Service.upload(boardImage, FileRootPathVO.BOARD_PATH);

        boardImageRepository.save(BoardImage.builder()
                .board(board)
                .accountProfileImage(boardImageFile)
                .build());
    }

    private Account findAccountByIdOrThrowException(Long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));
    }
}
