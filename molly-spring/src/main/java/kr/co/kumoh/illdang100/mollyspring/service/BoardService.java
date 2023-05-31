package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.image.BoardImage;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
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

        List<Long> boardImageIds = createPostRequest.getBoardImageIds();
        if (boardImageIds != null && !boardImageIds.isEmpty()) {
            List<BoardImage> findBoardImages = boardImageRepository.findAllById(boardImageIds);
            if (!findBoardImages.isEmpty()) {
                board.changeHasImage(true);

                findBoardImages.forEach(boardImage -> boardImage.changeBoardId(board.getId()));
            }
        }

        return new CreatePostResponse(board.getId());
    }

    @Transactional
    public void quitCreatePost(List<Long> boardImageIds) {

        if (boardImageIds != null) {
            List<BoardImage> findBoardImages = boardImageRepository.findAllById(boardImageIds);
            deleteBoardImagesInBatch(findBoardImages);
        }
    }

    @Transactional
    public AddBoardImageResponse addBoardImage(Long accountId, MultipartFile boardImage) {

        AddBoardImageResponse addBoardImageResponse = new AddBoardImageResponse();
        if (boardImage != null && !boardImage.isEmpty()) {
            try {
                BoardImage storedBoardImage =
                        saveBoardImageToTmp(boardImage, FileRootPathVO.BOARD_PATH + accountId);
                addBoardImageResponse.setBoardImageId(storedBoardImage.getId());
                addBoardImageResponse.setStoredBoardImageUrl(storedBoardImage.getBoardImageFile().getStoreFileUrl());
            } catch (Exception e) {
                throw new CustomApiException(e.getMessage());
            }
        }

        return addBoardImageResponse;
    }

    public Page<RetrievePostListDto> getPostList(RetrievePostListCondition retrievePostListCondition, Pageable pageable) {

        return boardRepository.findPagePostList(retrievePostListCondition, pageable);
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long boardId, Long accountId) {

        Board findBoard = findBoardByIdOrThrowException(boardId);

        findBoard.increaseViews();

        List<String> boardImages = getBoardImages(boardId);

        boolean isOwner = false;
        boolean thumbsUp = false;

        if (accountId != null) {
            thumbsUp = likyRepository.existsByAccountIdAndBoard_Id(accountId, boardId);
            isOwner = isAuthorizedToAccessBoard(findBoard, accountId);
        }

        List<BoardCommentDto> commentDtoList = getBoardCommentDtoList(boardId);

        return PostDetailResponse.builder()
                .isOwner(isOwner)
                .title(findBoard.getBoardTitle())
                .category(findBoard.getCategory().toString())
                .petType(findBoard.getPetType().toString())
                .boardImages(boardImages)
                .content(findBoard.getBoardContent())
                .writerNick(findBoard.getAccount().getNickname())
                .createdAt(findBoard.getCreatedDate())
                .views(findBoard.getViews())
                .writerProfileImage(extractAccountProfileUrlFromBoard(findBoard))
                .comments(commentDtoList)
                .thumbsUp(thumbsUp)
                .likyCnt(findBoard.getLikyCnt())
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
                            comment.getId(),
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
        return boardImageRepository.findByBoardId(boardId)
                .stream()
                .map(boardImage -> boardImage.getBoardImageFile().getStoreFileUrl())
                .collect(Collectors.toList());
    }

    private boolean isAuthorizedToAccessBoard(Board board, Long accountId) {
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

    @Transactional
    public LikyBoardResponse toggleLikePost(Long accountId, Long boardId) {

        Board findBoard = findBoardByIdOrThrowException(boardId);
        boolean thumbsUp;
        String message;
        if (likyRepository.existsByAccountIdAndBoard_Id(accountId, boardId)) {
            likyRepository.deleteByAccountIdAndBoard_Id(accountId, boardId);
            findBoard.decreaseLikyCnt();
            message = "게시글 좋아요 삭제";
            thumbsUp = false;
        } else {
            likyRepository.save(new Liky(findBoard, accountId));
            findBoard.increaseLikyCnt();
            message = "게시글 좋아요";
            thumbsUp = true;
        }

        return new LikyBoardResponse(findBoard.getLikyCnt(), thumbsUp, message);
    }

    @Transactional
    public void updatePost(Long boardId, Long accountId, UpdatePostRequest updatePostRequest) {

        Board findBoard = findBoardByIdOrThrowException(boardId);

        checkBoardAccessAuthorization(accountId, findBoard);

        findBoard.update(updatePostRequest);
    }

    @Transactional
    public void deletePost(Long boardId, Long accountId) {

        // TODO: 신고 테이블 필드 변경 (boardId -> null)

        Board findBoard = findBoardByIdOrThrowException(boardId);

        checkBoardAccessAuthorization(accountId, findBoard);

        deleteCommentsByBoardIdInBatch(boardId);
        deleteLikiesByBoardIdInBatch(boardId);
        deleteBoardImagesInBatch(boardImageRepository.findByBoardId(boardId));
        boardRepository.deleteById(boardId);
    }

    private void deleteBoardImagesInBatch(List<BoardImage> boardImages) {

        boardImages.forEach(boardImage -> {
            s3Service.delete(boardImage.getBoardImageFile().getStoreFileName());
        });

        boardImageRepository.deleteAllByIdInBatch(boardImages.stream()
                .map(BoardImage::getId)
                .collect(Collectors.toList()));
    }

    private void deleteLikiesByBoardIdInBatch(Long boardId) {
        likyRepository.deleteAllByIdInBatch(likyRepository.findByBoard_Id(boardId).stream()
                .map(Liky::getId)
                .collect(Collectors.toList()));
    }

    private void deleteCommentsByBoardIdInBatch(Long boardId) {
        commentRepository.deleteAllByIdInBatch(commentRepository.findByBoard_Id(boardId).stream()
                .map(Comment::getId)
                .collect(Collectors.toList()));
    }

    private BoardImage saveBoardImage(Long boardId, MultipartFile boardImage, String filePath) throws IOException {
        ImageFile boardImageFile =
                s3Service.upload(boardImage, filePath + boardId);

        return boardImageRepository.save(new BoardImage(boardId, boardImageFile));
    }

    private BoardImage saveBoardImageToTmp(MultipartFile boardImage, String filePath) throws IOException {
        ImageFile boardImageFile =
                s3Service.upload(boardImage, filePath);

        return boardImageRepository.save(new BoardImage(null, boardImageFile));
    }

    private Account findAccountByIdOrThrowException(Long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));
    }

    private Board findBoardByIdOrThrowException(Long boardId) {
        return boardRepository.findWithAccountById(boardId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 게시글입니다"));
    }

    private void checkBoardAccessAuthorization(Long accountId, Board findBoard) {
        if (!isAuthorizedToAccessBoard(findBoard, accountId)) {
            throw new CustomApiException("해당 게시글에 접근할 권한이 없습니다");
        }
    }
}
