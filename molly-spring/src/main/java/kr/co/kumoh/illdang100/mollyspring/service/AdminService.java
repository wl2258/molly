package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.BoardComplaint;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.CommentComplaint;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.ComplaintReasonEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.Suspension;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.SuspensionDate;
import kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.comment.CommentRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.complaint.BoardComplaintRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.complaint.CommentComplaintRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.suspension.SuspensionDateRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.suspension.SuspensionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AccountRepository accountRepository;
    private final SuspensionRepository suspensionRepository;
    private final SuspensionDateRepository suspensionDateRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardComplaintRepository boardComplaintRepository;
    private final CommentComplaintRepository commentComplaintRepository;

    public Slice<RetrieveComplaintListDto> getBoardComplaintList(Pageable pageable) {
        return boardComplaintRepository.searchSlice(pageable);
    }

    public Slice<RetrieveComplaintListDto> getCommentComplaintList(Pageable pageable) {
        return commentComplaintRepository.searchSlice(pageable);
    }

    public ComplaintDetailResponse getBoardComplaintDetail(Long boardComplaintId) {

        BoardComplaint boardComplaint = findBoardComplaintByIdOrThrowException(boardComplaintId);

        return ComplaintDetailResponse.builder()
                .complaintId(boardComplaint.getId())
                .reportedItemId(boardComplaint.getBoard().getId())
                .reporterEmail(boardComplaint.getReporterEmail())
                .reportedEmail(boardComplaint.getReportedEmail())
                .createdAt(boardComplaint.getCreatedDate())
                .reason(boardComplaint.getComplaintReason().getValue())
                .build();
    }

    public ComplaintDetailResponse getCommentComplaintDetail(Long commentComplaintId) {

        CommentComplaint commentComplaint = findCommentComplaintByIdOrThrowException(commentComplaintId);

        return ComplaintDetailResponse.builder()
                .complaintId(commentComplaint.getId())
                .reportedItemId(commentComplaint.getComment().getId())
                .reporterEmail(commentComplaint.getReporterEmail())
                .reportedEmail(commentComplaint.getReportedEmail())
                .createdAt(commentComplaint.getCreatedDate())
                .reason(commentComplaint.getComplaintReason().getValue())
                .build();
    }

    @Transactional
    public void suspendAccount(Long accountId, Long boardId, Long commentId, SuspendAccountRequest suspendAccountRequest) {

        // TODO: 어떤 신고로부터 정지당하는 건지 받아서 해당 신고 전부 삭제하기!!

        Account findAccount = findAccountByIdOrThrowException(accountId);

        // 동일한 컨텐츠(게시글 혹은 댓글)에 대해 정지가 존재하면 안됨 -> 찾아야함
        if (boardId != null && commentId == null) {
            if (suspensionRepository.existsByBoardId(boardId)) {
                return;
            }
            findBoardByIdOrThrowException(boardId);
            saveSuspension(findAccount.getEmail(), boardId, null, suspendAccountRequest);

        } else if (boardId == null && commentId != null) {
            if (suspensionRepository.existsByCommentId(commentId)) {
                return;
            }
            findCommentByIdOrThrowException(commentId);
            saveSuspension(findAccount.getEmail(), null, commentId, suspendAccountRequest);
        }

        updateSuspensionDate(findAccount.getEmail(), suspendAccountRequest.getSuspensionPeriod());
    }

    private void saveSuspension(String accountEmail, Long boardId, Long commentId, SuspendAccountRequest suspendAccountRequest) {
        // TODO: 들어온 boardId 또는 commentId가 해당 사용자가 작성한게 맞는지 검사하는 기능 추가
        suspensionRepository.save(Suspension.builder()
                .accountEmail(accountEmail)
                .boardId(boardId)
                .commentId(commentId)
                .suspensionPeriod(suspendAccountRequest.getSuspensionPeriod())
                .complaintReason(ComplaintReasonEnum.valueOf(suspendAccountRequest.getReason()))
                .build());

        log.info("정지가 성공적으로 생성되었습니다. [accountEmail={}, suspensionExpiryDate={}]", accountEmail, suspendAccountRequest.getSuspensionPeriod());
    }

    private void updateSuspensionDate(String accountEmail, Long suspensionPeriod) {
        SuspensionDate sd = suspensionDateRepository.findByAccountEmail(accountEmail)
                .map(suspensionDate -> {
                    if (!LocalDate.now().isAfter(suspensionDate.getSuspensionExpiryDate())) {
                        suspensionDate.changeSuspensionExpiryDate(suspensionDate.getSuspensionExpiryDate().plusDays(suspensionPeriod));
                    } else {
                        suspensionDate.changeSuspensionExpiryDate(LocalDate.now().plusDays(suspensionPeriod));
                    }
                    return suspensionDate;
                })
                .orElseGet(() -> suspensionDateRepository.save(new SuspensionDate(accountEmail, LocalDate.now().plusDays(suspensionPeriod))));

        log.info("정지 기간이 성공적으로 업데이트되었습니다. [accountEmail={}, suspensionExpiryDate={}]", accountEmail, sd.getSuspensionExpiryDate());
    }

    // TODO: 관리자한테 사용자 리스트 뿌려주는 기능
    // TODO: 관리자가 사용자 이메일로 검색하는 기능
    // TODO: 관리자한테 신고 목록 뿌려주는 기능

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
                .orElseThrow(() -> new CustomApiException("존재하지 않는 댓긆입니다"));
    }

    private BoardComplaint findBoardComplaintByIdOrThrowException(Long boardComplaintId) {
        return boardComplaintRepository.findById(boardComplaintId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 신고입니다"));
    }

    private CommentComplaint findCommentComplaintByIdOrThrowException(Long commentComplaintId) {
        return commentComplaintRepository.findById(commentComplaintId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 신고입니다"));
    }
}
