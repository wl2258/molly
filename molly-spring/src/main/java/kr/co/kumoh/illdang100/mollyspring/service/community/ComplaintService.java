package kr.co.kumoh.illdang100.mollyspring.service.community;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.Complaint;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.ComplaintReasonEnum;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.complaint.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComplaintService {

    private final AccountRepository accountRepository;
    private final ComplaintRepository complaintRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시글 신고
     *
     * @param boardId   신고하고자 하는 게시글PK
     * @param accountId 신고자PK
     */
    @Transactional
    public void reportPost(Long boardId, Long accountId, String reason) {

        Account reporter = findAccountByIdOrThrowException(accountId);
        findBoardByIdOrThrowException(boardId);

        Complaint complaint = complaintRepository.save(Complaint.builder()
                .reporterEmail(reporter.getEmail())
                .boardId(boardId)
                .complaintReason(ComplaintReasonEnum.valueOf(reason))
                .build());
    }

    @Transactional
    public void reportComment(Long commentId, Long accountId, String reason) {

        Account reporter = findAccountByIdOrThrowException(accountId);
        findCommentByIdOrThrowException(commentId);

        Complaint complaint = complaintRepository.save(Complaint.builder()
                .reporterEmail(reporter.getEmail())
                .commentId(commentId)
                .complaintReason(ComplaintReasonEnum.valueOf(reason))
                .build());
    }

    private Account findAccountByIdOrThrowException(Long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));
    }

    private Board findBoardByIdOrThrowException(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 게시글입니다"));
    }

    private Comment findCommentByIdOrThrowException(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 댓글입니다"));
    }
}
