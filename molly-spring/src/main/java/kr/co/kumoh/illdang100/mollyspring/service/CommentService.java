package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.kumoh.illdang100.mollyspring.dto.comment.CommentReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.comment.CommentRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest, Long accountId, Long boardId) {

        Board findBoard = findBoardByIdOrThrowException(boardId);
        Comment comment =
                commentRepository.save(new Comment(findBoard, createCommentRequest.getCommentContent(), accountId));
        findBoard.increaseCommentCnt();

        return new CreateCommentResponse(comment.getId());
    }

    @Transactional
    public void deleteComment(Long accountId, Long boardId, Long commentId) {

        Comment findComment = findCommentByIdOrThrowException(commentId);

        if (!isCommentAuthor(accountId, findComment)) {
            throw new CustomApiException("댓글을 삭제할 권한이 없습니다.");
        }

        Board findBoard = findBoardByIdOrThrowException(boardId);
        findBoard.decreaseCommentCnt();

        commentRepository.delete(findComment);
    }

    private static boolean isCommentAuthor(Long accountId, Comment findComment) {
        return findComment.getAccountId().longValue() == accountId.longValue();
    }

    private Comment findCommentByIdOrThrowException(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 댓글입니다."));
    }

    private Board findBoardByIdOrThrowException(Long boardId) {
        return boardRepository.findWithAccountById(boardId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 게시글입니다"));
    }
}
