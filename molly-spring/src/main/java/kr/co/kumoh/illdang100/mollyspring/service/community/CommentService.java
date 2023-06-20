package kr.co.kumoh.illdang100.mollyspring.service.community;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.CommentComplaint;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.complaint.CommentComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.comment.CommentReqDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentComplaintRepository commentComplaintRepository;

    @Transactional
    public BoardCommentDto createComment(CreateCommentRequest createCommentRequest, Long accountId, Long boardId) {

        Board findBoard = findBoardByIdOrThrowException(boardId);
        Account findAccount = findAccountByIdOrThrowException(accountId);
        Comment comment =
                commentRepository.save(new Comment(findBoard, createCommentRequest.getCommentContent(), findAccount.getEmail()));
        findBoard.increaseCommentCnt();

        String profileImageUrl = null;
        if (findAccount.getAccountProfileImage() != null) {
            profileImageUrl = findAccount.getAccountProfileImage().getStoreFileUrl();
        }

        return BoardCommentDto.builder()
                .commentId(comment.getId())
                .commentOwner(true)
                .commentAccountEmail(comment.getAccountEmail())
                .commentWriteNick(findAccount.getNickname())
                .commentCreatedAt(comment.getCreatedDate())
                .content(comment.getCommentContent())
                .commentProfileImage(profileImageUrl)
                .build();
    }


    @Transactional
    public void deleteComment(Long accountId, Long boardId, Long commentId) {

        Account findAccount = findAccountByIdOrThrowException(accountId);
        Comment findComment = findCommentByIdOrThrowException(commentId);

        if (!isCommentAuthor(findAccount.getEmail(), findComment)) {
            throw new CustomApiException("댓글을 삭제할 권한이 없습니다.");
        }

        Board findBoard = findBoardByIdOrThrowException(boardId);
        findBoard.decreaseCommentCnt();

        deleteCommentComplaintsByCommentIdInBatch(findComment.getId());
        commentRepository.delete(findComment);
    }

    private void deleteCommentComplaintsByCommentIdInBatch(Long commentId) {
        List<Long> complaintIds = commentComplaintRepository.findByComment_Id(commentId)
                .stream()
                .map(CommentComplaint::getId)
                .collect(Collectors.toList());
        commentComplaintRepository.deleteAllByIdInBatch(complaintIds);
    }

    private static boolean isCommentAuthor(String accountEmail, Comment comment) {
        return comment.getAccountEmail().equals(accountEmail);
    }

    private Account findAccountByIdOrThrowException(Long commentId) {
        return accountRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다."));
    }

    private Comment findCommentByIdOrThrowException(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 댓글입니다."));
    }

    private Board findBoardByIdOrThrowException(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 게시글입니다"));
    }
}
