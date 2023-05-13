package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.image.BoardImage;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.image.BoardImageRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.liky.LikyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    @Transactional
    public PostDetailResponse getPostDetail(Long boardId, Long accountId) {

        Board board = boardRepository.findWithAccountById(boardId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 게시글입니다"));

        board.increaseViews();

        List<String> boardImages = getBoardImages(boardId);

        boolean isOwner = false;
        boolean thumbsUp = false;

        if (accountId != null) {
            thumbsUp = likyRepository.existsByAccountIdAndBoard_Id(accountId, boardId);
            isOwner = isAccountOfBoard(board, accountId);
        }

        List<BoardCommentDto> commentDtoList = getBoardCommentDtoList(boardId);

        return PostDetailResponse.builder()
                .isOwner(isOwner)
                .title(board.getBoardTitle())
                .boardImages(boardImages)
                .content(board.getBoardContent())
                .writerNick(board.getAccount().getNickname())
                .createdAt(board.getCreatedDate())
                .views(board.getViews())
                .writerProfileImage(extractAccountProfileUrlFromBoard(board))
                .comments(commentDtoList)
                .thumbsUp(thumbsUp)
                .likyCnt(board.getLikyCnt())
                .build();
    }

    private List<BoardCommentDto> getBoardCommentDtoList(Long boardId) {
        List<Comment> comments = commentRepository.findByBoard_IdOrderByCreatedDate(boardId);
        List<Long> commentAccountIds = comments.stream().map(Comment::getAccountId).collect(Collectors.toList());
        Map<Long, Account> accountMap = accountRepository.findByIdIn(commentAccountIds)
                .stream().collect(Collectors.toMap(Account::getId, account -> account));

        return comments.stream()
                .map(comment -> {
                    Account account = accountMap.get(comment.getAccountId());

                    String nickname = account != null ? account.getNickname() : null;

                    String commentProfileImageUrl = account != null && account.getAccountProfileImage() != null
                            ? account.getAccountProfileImage().getStoreFileUrl() : null;

                    return new BoardCommentDto(
                            comment.getAccountId(),
                            nickname,
                            comment.getCreatedDate(),
                            comment.getCommentContent(),
                            commentProfileImageUrl
                    );
                })
                .collect(Collectors.toList());
    }

    private List<String> getBoardImages(Long boardId) {
        return boardImageRepository.findByBoard_id(boardId)
                .stream()
                .map(boardImage -> boardImage.getAccountProfileImage().getStoreFileUrl())
                .collect(Collectors.toList());
    }

    private static boolean isAccountOfBoard(Board board, Long accountId) {
        return board.getAccount().getId().equals(accountId);
    }

    private String extractAccountProfileUrlFromBoard(Board board) {
        if (hasAccountProfileImageFromBoard(board)) {
            return board.getAccount().getAccountProfileImage().getStoreFileUrl();
        } else {
            return null;
        }
    }

    private boolean hasAccountProfileImageFromBoard(Board board) {
        return board.getAccount().getAccountProfileImage() != null;
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
