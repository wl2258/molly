package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.BoardComplaint;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.CommentComplaint;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.ComplaintReasonEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.Suspension;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.SuspensionDate;
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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 게시글에 대한 신고 목록 조회
     * @return 게시글에 대한 신고 목록
     */
    public Slice<RetrieveComplaintListDto> getBoardComplaintList(Pageable pageable) {
        return boardComplaintRepository.searchSlice(pageable);
    }

    /**
     * 댓글에 대한 신고 목록 조회
     * @return 댓글에 대한 신고 목록
     */
    public Slice<RetrieveComplaintListDto> getCommentComplaintList(Pageable pageable) {
        return commentComplaintRepository.searchSlice(pageable);
    }

    /**
     * 게시글에 대한 신고 상세조회
     * @param boardComplaintId 게시글에 대한 신고 PK
     * @return 신고 상세 정보
     */
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

    /**
     * 댓글에 대한 신고 상세조회
     * @param commentComplaintId 댓글에 대한 신고 PK
     * @return 신고 상세 정보
     */
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

    /**
     * 사용자 정지
     * @param accountId 신고 당하는 사용자 PK
     * @param boardId 신고 당하는 게시글 PK
     * @param commentId 신고 당하는 댓글 PK
     * @param suspendAccountRequest 정지 기간 및 정지 사유
     */
    @Transactional
    public void suspendAccount(Long accountId, Long boardId, Long commentId, SuspendAccountRequest suspendAccountRequest) {

        Account findAccount = findAccountByIdOrThrowException(accountId);

        // 동일한 컨텐츠(게시글 혹은 댓글)에 대해 정지가 존재하면 안된다.
        if (boardId != null && commentId == null) {
            if (suspensionRepository.existsByBoardId(boardId)) {
                throw new CustomApiException("이미 존재하는 정지입니다");
            }
            Board findBoard = findBoardByIdOrThrowException(boardId);
            // 들어온 boardId 또는 commentId가 해당 사용자가 작성한게 맞는지 검사하는 기능 추가
            checkAccessAuthorization(findBoard.getAccountEmail(), findAccount.getEmail());
            saveSuspension(findAccount.getEmail(), boardId, null, suspendAccountRequest);
            // 어떤 신고로부터 정지당하는 건지 받아서 해당 신고 전부 삭제하기!!
            deleteBoardComplaintsByBoardIdInBatch(findBoard.getId());

        } else if (boardId == null && commentId != null) {
            if (suspensionRepository.existsByCommentId(commentId)) {
                throw new CustomApiException("이미 존재하는 정지입니다");
            }
            Comment findComment = findCommentByIdOrThrowException(commentId);
            // 들어온 boardId 또는 commentId가 해당 사용자가 작성한게 맞는지 검사하는 기능 추가
            checkAccessAuthorization(findComment.getAccountEmail(), findAccount.getEmail());
            saveSuspension(findAccount.getEmail(), null, commentId, suspendAccountRequest);
            // 어떤 신고로부터 정지당하는 건지 받아서 해당 신고 전부 삭제하기!!
            deleteCommentComplaintsByBoardIdInBatch(findComment.getId());
        }

        updateSuspensionDate(findAccount.getEmail(), suspendAccountRequest.getSuspensionPeriod());
    }

    private void saveSuspension(String accountEmail, Long boardId, Long commentId, SuspendAccountRequest suspendAccountRequest) {
        suspensionRepository.save(Suspension.builder()
                .accountEmail(accountEmail)
                .boardId(boardId)
                .commentId(commentId)
                .suspensionPeriod(suspendAccountRequest.getSuspensionPeriod())
                .complaintReason(ComplaintReasonEnum.valueOf(suspendAccountRequest.getReason()))
                .build());

        log.info("정지가 성공적으로 생성되었습니다. [accountEmail={}, suspensionExpiryDate={}]", accountEmail, suspendAccountRequest.getSuspensionPeriod());
    }

    private void checkAccessAuthorization(String writerEmail, String accountEmail) {
        if (!writerEmail.equals(accountEmail)) {
            throw new CustomApiException("해당 사용자가 작성한 게시물이 아닙니다");
        }
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

    private <T> T findByIdOrThrowException(Long id, JpaRepository<T, Long> repository, String exceptionMessage) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomApiException(exceptionMessage));
    }

    private Account findAccountByIdOrThrowException(Long accountId) {
        return findByIdOrThrowException(accountId, accountRepository, "존재하지 않는 사용자입니다");
    }

    private Board findBoardByIdOrThrowException(Long boardId) {
        return findByIdOrThrowException(boardId, boardRepository, "존재하지 않는 게시글입니다");
    }

    private Comment findCommentByIdOrThrowException(Long commentId) {
        return findByIdOrThrowException(commentId, commentRepository, "존재하지 않는 댓글입니다");
    }

    private BoardComplaint findBoardComplaintByIdOrThrowException(Long boardComplaintId) {
        return findByIdOrThrowException(boardComplaintId, boardComplaintRepository, "존재하지 않는 신고입니다");
    }

    private CommentComplaint findCommentComplaintByIdOrThrowException(Long commentComplaintId) {
        return findByIdOrThrowException(commentComplaintId, commentComplaintRepository, "존재하지 않는 신고입니다");
    }

    private void deleteBoardComplaintsByBoardIdInBatch(Long boardId) {
        List<Long> complaintIds = boardComplaintRepository.findByBoard_Id(boardId)
                .stream()
                .map(BoardComplaint::getId)
                .collect(Collectors.toList());
        boardComplaintRepository.deleteAllByIdInBatch(complaintIds);
    }

    private void deleteCommentComplaintsByBoardIdInBatch(Long commentId) {
        List<Long> complaintIds = commentComplaintRepository.findByComment_Id(commentId)
                .stream()
                .map(CommentComplaint::getId)
                .collect(Collectors.toList());
        commentComplaintRepository.deleteAllByIdInBatch(complaintIds);
    }
}
