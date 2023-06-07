package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AdminApiController {

    private final AdminService adminService;

    @GetMapping("/admin/board-complaints")
    public ResponseEntity<?> retrieveBoardComplaintList(@PageableDefault(size = 7, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Slice<RetrieveComplaintListDto> retrieveComplaintListDto = adminService.getBoardComplaintList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 신고 목록 조회에 성공했습니다", retrieveComplaintListDto), HttpStatus.OK);
    }

    @GetMapping("/admin/comment-complaints")
    public ResponseEntity<?> retrieveCommentComplaintList(@PageableDefault(size = 7, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Slice<RetrieveComplaintListDto> retrieveComplaintListDto = adminService.getCommentComplaintList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 신고 목록 조회에 성공했습니다", retrieveComplaintListDto), HttpStatus.OK);
    }

    @GetMapping("/admin/board-complaint/{id}")
    public ResponseEntity<?> retrieveBoardComplaintDetail(@PathVariable("id") Long boardComplaintId) {

        ComplaintDetailResponse boardComplaintDetail = adminService.getBoardComplaintDetail(boardComplaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 신고 상세 조회에 성공했습니다", boardComplaintDetail), HttpStatus.OK);
    }

    @GetMapping("/admin/comment-complaint/{id}")
    public ResponseEntity<?> retrieveCommentComplaintDetail(@PathVariable("id") Long commentComplaintId) {

        ComplaintDetailResponse commentComplaintDetail = adminService.getCommentComplaintDetail(commentComplaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 신고 상세 조회에 성공했습니다", commentComplaintDetail), HttpStatus.OK);
    }

    @PostMapping("/admin/account/{accountId}/suspend/board/{boardId}")
    public ResponseEntity<?> suspendAccountByBoard(@PathVariable("accountId") Long accountId,
                                                   @PathVariable("boardId") Long boardId,
                                                   @RequestBody SuspendAccountRequest suspendAccountRequest) {

        adminService.suspendAccount(accountId, boardId, null, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }

    @PostMapping("/admin/account/{accountId}/suspend/comment/{commentId}")
    public ResponseEntity<?> suspendAccountByComment(@PathVariable("accountId") Long accountId,
                                                     @PathVariable("commentId") Long commentId,
                                                     @RequestBody SuspendAccountRequest suspendAccountRequest) {

        adminService.suspendAccount(accountId, null, commentId, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }
}
