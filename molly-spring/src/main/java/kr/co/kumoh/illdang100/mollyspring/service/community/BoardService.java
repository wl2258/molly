package kr.co.kumoh.illdang100.mollyspring.service.community;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.BoardComplaint;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.CommentComplaint;
import kr.co.kumoh.illdang100.mollyspring.domain.image.BoardImage;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.complaint.BoardComplaintRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.complaint.CommentComplaintRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.image.BoardImageRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.liky.LikyRepository;
import kr.co.kumoh.illdang100.mollyspring.service.FileRootPathVO;
import kr.co.kumoh.illdang100.mollyspring.service.S3Service;
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
    private final BoardComplaintRepository boardComplaintRepository;
    private final CommentComplaintRepository commentComplaintRepository;

    @Transactional
    public CreatePostResponse createPost(Long accountId, CreatePostRequest createPostRequest, boolean isNotice) {

        Account findAccount = findAccountByIdOrThrowException(accountId);

        Board board = boardRepository.save(new Board(findAccount, createPostRequest, isNotice));

        List<Long> boardImageIds = createPostRequest.getBoardImageIds();
        if (boardImageIds != null && !boardImageIds.isEmpty()) {
            List<BoardImage> findBoardImages = boardImageRepository.findAllById(boardImageIds);
            if (!findBoardImages.isEmpty()) {
                board.changeHasImage(true);

                findBoardImages.forEach(boardImage -> boardImage.changeBoard(board));
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
                        saveBoardImage(boardImage, FileRootPathVO.BOARD_PATH + accountId);
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

        boolean isOwner = false;
        boolean thumbsUp = false;
        String writerNick = null;
        String writerProfileImage = null;

        if (accountId != null) {
            Account findAccount = findAccountByIdOrThrowException(accountId);
            thumbsUp = likyRepository.existsByAccountEmailAndBoard_Id(findAccount.getEmail(), boardId);
            isOwner = isAuthorizedToAccessBoard(findBoard, accountId);
        }

        if (findBoard.getAccount() != null) {
            writerNick = findBoard.getAccount().getNickname();
            writerProfileImage = extractAccountProfileUrlFromBoard(findBoard);
        }

        List<BoardCommentDto> commentDtoList = getBoardCommentDtoList(boardId, accountId);

        return PostDetailResponse.builder()
                .boardOwner(isOwner)
                .title(findBoard.getBoardTitle())
                .category(findBoard.getCategory().toString())
                .petType(findBoard.getPetType().toString())
                .content(findBoard.getBoardContent())
                .writerNick(writerNick)
                .writerEmail(findBoard.getAccountEmail())
                .createdAt(findBoard.getCreatedDate())
                .views(findBoard.getViews())
                .writerProfileImage(writerProfileImage)
                .comments(commentDtoList)
                .thumbsUp(thumbsUp)
                .likyCnt(findBoard.getLikyCnt())
                .build();
    }


    private List<BoardCommentDto> getBoardCommentDtoList(Long boardId, Long accountId) {
        // TODO: 댓글 페이징 기능
        List<Comment> comments = commentRepository.findByBoard_IdOrderByCreatedDate(boardId);
        List<String> commentAccountEmails = comments.stream().map(Comment::getAccountEmail).distinct().collect(Collectors.toList());
        Map<String, Account> accountMap = accountRepository.findByEmailIn(commentAccountEmails)
                .stream().collect(Collectors.toMap(Account::getEmail, account -> account));

        Account findAccount = (accountId != null) ? findAccountByIdOrThrowException(accountId) : null;

        return comments.stream()
                .map(comment -> createBoardCommentDto(comment, accountMap, findAccount))
                .collect(Collectors.toList());
    }

    private BoardCommentDto createBoardCommentDto(Comment comment, Map<String, Account> accountMap, Account findAccount) {
        Account account = accountMap.get(comment.getAccountEmail());
        boolean commentOwner = (account != null && findAccount != null && account.getId().equals(findAccount.getId()));
        String nickname = (account != null) ? account.getNickname() : null;
        String commentProfileImageUrl = (account != null && account.getAccountProfileImage() != null)
                ? account.getAccountProfileImage().getStoreFileUrl() : null;

        return new BoardCommentDto(
                comment.getId(),
                commentOwner,
                comment.getAccountEmail(),
                nickname,
                comment.getCreatedDate(),
                comment.getCommentContent(),
                commentProfileImageUrl
        );
    }

    private boolean isAuthorizedToAccessBoard(Board board, Long accountId) {
        boolean result = false;
        Account account = board.getAccount();
        if (account != null) {
            result = board.getAccount().getId().equals(accountId);
        }
        return result;
    }

    public String extractAccountProfileUrlFromBoard(Board board) {
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

        String email = findAccountByIdOrThrowException(accountId).getEmail();
        Board findBoard = findBoardByIdOrThrowException(boardId);
        boolean thumbsUp;
        String message;
        if (likyRepository.existsByAccountEmailAndBoard_Id(email, boardId)) {
            likyRepository.deleteByAccountEmailAndBoard_Id(email, boardId);
            findBoard.decreaseLikyCnt();
            message = "게시글 좋아요 삭제";
            thumbsUp = false;
        } else {
            likyRepository.save(new Liky(findBoard, email));
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

        Board findBoard = findBoardByIdOrThrowException(boardId);

        checkBoardAccessAuthorization(accountId, findBoard);

        deleteCommentsByBoardIdInBatch(boardId);
        deleteLikiesByBoardIdInBatch(boardId);
        deleteBoardImagesIfNotEmpty(boardId);
        deleteBoardComplaintsByBoardIdInBatch(boardId);
        boardRepository.deleteById(boardId);
    }

    public void deleteBoardImagesIfNotEmpty(Long boardId) {
        List<BoardImage> findBoardImages = boardImageRepository.findByBoardId(boardId);
        if (!findBoardImages.isEmpty()) {
            deleteBoardImagesInBatch(findBoardImages);
        }
    }

    private void deleteBoardComplaintsByBoardIdInBatch(Long boardId) {
        List<Long> complaintIds = boardComplaintRepository.findByBoard_Id(boardId)
                .stream()
                .map(BoardComplaint::getId)
                .collect(Collectors.toList());
        boardComplaintRepository.deleteAllByIdInBatch(complaintIds);
    }

    private void deleteBoardImagesInBatch(List<BoardImage> boardImages) {

        List<String> storeFileNames = boardImages.stream()
                .map(boardImage -> boardImage.getBoardImageFile().getStoreFileName())
                .collect(Collectors.toList());

        s3Service.deleteFiles(storeFileNames);

        boardImageRepository.deleteAllByIdInBatch(boardImages.stream()
                .map(BoardImage::getId)
                .collect(Collectors.toList()));
    }

    public void deleteLikiesByBoardIdInBatch(Long boardId) {
        likyRepository.deleteAllByIdInBatch(likyRepository.findByBoard_Id(boardId).stream()
                .map(Liky::getId)
                .collect(Collectors.toList()));
    }

    public void deleteCommentsByBoardIdInBatch(Long boardId) {
        List<Comment> comments = commentRepository.findByBoard_Id(boardId);

        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());

        deleteCommentComplaintsByCommentIdInBatch(commentIds);

        commentRepository.deleteAllByIdInBatch(commentIds);
    }

    private void deleteCommentComplaintsByCommentIdInBatch(List<Long> commentIds) {
        List<Long> complaintIds = commentComplaintRepository.findByComment_IdIn(commentIds)
                .stream()
                .map(CommentComplaint::getId)
                .collect(Collectors.toList());
        commentComplaintRepository.deleteAllByIdInBatch(complaintIds);
    }

    private BoardImage saveBoardImage(MultipartFile boardImage, String filePath) throws IOException {
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
