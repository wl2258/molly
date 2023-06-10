package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AdminApiController {

    private final AdminService adminService;

    /**
     * 게시글에 대한 신고 목록 조회
     * @return 게시글에 대한 신고 목록
     */
    @GetMapping("/admin/board-complaints")
    public ResponseEntity<?> retrieveBoardComplaintList(@PageableDefault(size = 7, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Slice<RetrieveComplaintListDto> retrieveComplaintListDto = adminService.getBoardComplaintList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 신고 목록 조회에 성공했습니다", retrieveComplaintListDto), HttpStatus.OK);
    }

    /**
     * 댓글에 대한 신고 목록 조회
     * @return 댓글에 대한 신고 목록
     */
    @GetMapping("/admin/comment-complaints")
    public ResponseEntity<?> retrieveCommentComplaintList(@PageableDefault(size = 7, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Slice<RetrieveComplaintListDto> retrieveComplaintListDto = adminService.getCommentComplaintList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 신고 목록 조회에 성공했습니다", retrieveComplaintListDto), HttpStatus.OK);
    }

    /**
     * 게시글에 대한 신고 상세조회
     * @param boardComplaintId 게시글에 대한 신고 PK
     * @return 신고 상세 정보
     */
    @GetMapping("/admin/board-complaint/{id}")
    public ResponseEntity<?> retrieveBoardComplaintDetail(@PathVariable("id") Long boardComplaintId) {

        ComplaintDetailResponse boardComplaintDetail = adminService.getBoardComplaintDetail(boardComplaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 신고 상세 조회에 성공했습니다", boardComplaintDetail), HttpStatus.OK);
    }

    /**
     * 댓글에 대한 신고 상세조회
     * @param commentComplaintId 댓글에 대한 신고 PK
     * @return 신고 상세 정보
     */
    @GetMapping("/admin/comment-complaint/{id}")
    public ResponseEntity<?> retrieveCommentComplaintDetail(@PathVariable("id") Long commentComplaintId) {

        ComplaintDetailResponse commentComplaintDetail = adminService.getCommentComplaintDetail(commentComplaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 신고 상세 조회에 성공했습니다", commentComplaintDetail), HttpStatus.OK);
    }

    /**
     * 사용자 정지
     * @param accountId 신고 당하는 사용자 PK
     * @param boardId 신고 당하는 게시글 PK
     * @param suspendAccountRequest 정지 기간 및 정지 사유
     */
    @PostMapping("/admin/account/{accountId}/suspend/board/{boardId}")
    public ResponseEntity<?> suspendAccountByBoard(@PathVariable("accountId") Long accountId,
                                                   @PathVariable("boardId") Long boardId,
                                                   @RequestBody SuspendAccountRequest suspendAccountRequest) {

        adminService.suspendAccount(accountId, boardId, null, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }

    /**
     * 사용자 정지
     * @param accountId 신고 당하는 사용자 PK
     * @param commentId 신고 당하는 댓글 PK
     * @param suspendAccountRequest 정지 기간 및 정지 사유
     */
    @PostMapping("/admin/account/{accountId}/suspend/comment/{commentId}")
    public ResponseEntity<?> suspendAccountByComment(@PathVariable("accountId") Long accountId,
                                                     @PathVariable("commentId") Long commentId,
                                                     @RequestBody SuspendAccountRequest suspendAccountRequest) {

        adminService.suspendAccount(accountId, null, commentId, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }

    // TODO: 신고 상세조회에서 이동 버튼 누르면 이동하는 기능

    // TODO: 신고 삭제

    /**
     * 게시글 신고 삭제
     * @param complaintId 신고 PK
     */
    @DeleteMapping("/admin/board-complaint/{complaintId}")
    public ResponseEntity<?> deleteBoardComplaint(@PathVariable("complaintId") Long complaintId) {

        adminService.deleteBoardComplaint(complaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 신고 삭제에 성공했습니다", null), HttpStatus.OK);
    }

    /**
     * 댓글 신고 삭제
     * @param complaintId 신고 PK
     */
    @DeleteMapping("/admin/comment-complaint/{complaintId}")
    public ResponseEntity<?> deleteCommentComplaint(@PathVariable("complaintId") Long complaintId) {

        adminService.deleteCommentComplaint(complaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 신고 삭제에 성공했습니다", null), HttpStatus.OK);
    }

    // TODO: 관리자 커뮤니티 목록 페이지

    // TODO: 관리자 커뮨티 게시글 상세조회 페이지

    /**
     * 관리자용 게시글 상세 조회
     *
     * @param boardId   게시글 PK
     * @param principalDetails 인증된 사용자 정보
     * @return 게시글 상세 정보(게시글, 게시글 이미지, 댓글, 좋아요)
     */
    @GetMapping("/admin/board/{boardId}")
    public ResponseEntity<?> retrievePostDetail(@PathVariable("boardId") Long boardId,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) {

        PostDetailForAdminResponse postDetailForAdminResponse =
                adminService.getPostDetail(boardId, principalDetails.getAccount().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 상세 조회에 성공했습니다", postDetailForAdminResponse), HttpStatus.OK);
    }

    // TODO: 관리자가 특정 게시글 정지(o), 삭제, 수정 기능

    // 게시글 수정
    /**
     * 게시글 수정
     *
     * @param boardId           게시글PK
     * @param updatePostRequest 수정한 게시글 정보
     * @param principalDetails  인증된 사용자 정보
     */
    @PutMapping("/admin/board/{boardId}")
    public ResponseEntity<?> editPost(@PathVariable("boardId") Long boardId,
                                      @RequestBody BoardReqDto.UpdatePostRequest updatePostRequest,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {

//        boardService.updatePost(boardId, principalDetails.getAccount().getId(), updatePostRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정에 성공했습니다", null), HttpStatus.OK);
    }

    // 게시글 삭제
}
