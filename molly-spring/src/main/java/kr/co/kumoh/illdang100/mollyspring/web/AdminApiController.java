package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.service.AdminService;
import kr.co.kumoh.illdang100.mollyspring.service.community.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AdminApiController {

    private final AdminService adminService;
    private final BoardService boardService;

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

        BoardComplaintDetailResponse boardComplaintDetail = adminService.getBoardComplaintDetail(boardComplaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 신고 상세 조회에 성공했습니다", boardComplaintDetail), HttpStatus.OK);
    }

    /**
     * 댓글에 대한 신고 상세조회
     * @param commentComplaintId 댓글에 대한 신고 PK
     * @return 신고 상세 정보
     */
    @GetMapping("/admin/comment-complaint/{id}")
    public ResponseEntity<?> retrieveCommentComplaintDetail(@PathVariable("id") Long commentComplaintId) {

        CommentComplaintDetailResponse commentComplaintDetail = adminService.getCommentComplaintDetail(commentComplaintId);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 신고 상세 조회에 성공했습니다", commentComplaintDetail), HttpStatus.OK);
    }

    /**
     * 사용자 정지
     * @param boardId 신고 당하는 게시글 PK
     * @param suspendAccountRequest 정지에 관한 정보
     */
    @PostMapping("/admin/suspend/board/{boardId}")
    public ResponseEntity<?> suspendAccountByBoard(@PathVariable("boardId") Long boardId,
                                                   @RequestBody SuspendAccountRequest suspendAccountRequest) {

        adminService.suspendAccount(boardId, null, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }

    /**
     * 사용자 정지
     * @param commentId 신고 당하는 댓글 PK
     * @param suspendAccountRequest 정지에 관한 정보
     */
    @PostMapping("/admin/suspend/comment/{commentId}")
    public ResponseEntity<?> suspendAccountByComment(@PathVariable("commentId") Long commentId,
                                                     @RequestBody SuspendAccountRequest suspendAccountRequest) {

        log.debug("start suspensionAccountByComment");
        adminService.suspendAccount(null, commentId, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }

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

    /**
     * 관리자용 게시글 작성
     *
     * @param createPostRequest 게시글 정보
     * @param principalDetails  인증된 사용자 정보
     * @return 저장된 게시글 PK
     */
    @PostMapping("/admin/board")
    public ResponseEntity<?> createNewPost(@RequestBody @Valid CreatePostRequest createPostRequest,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        BoardRespDto.CreatePostResponse createPostResponse
                = boardService.createPost(principalDetails.getAccount().getId(), createPostRequest, true);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 작성에 성공했습니다", createPostResponse), HttpStatus.CREATED);
    }

    /**
     * 관리자용 게시글 수정
     *
     * @param boardId           게시글PK
     * @param updatePostRequest 수정한 게시글 정보
     */
    @PutMapping("/admin/board/{boardId}")
    public ResponseEntity<?> editPost(@PathVariable("boardId") Long boardId,
                                      @RequestBody UpdatePostRequest updatePostRequest) {

        adminService.updatePost(boardId, updatePostRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정에 성공했습니다", null), HttpStatus.OK);
    }

    /**
     * 관리자용 게시글 삭제
     *
     * @param boardId          삭제하고자 하는 게시글 PK
     * @param principalDetails 인증된 사용자 정보
     */
    @DeleteMapping("/admin/board/{boardId}")
    public ResponseEntity<?> deletePost(@PathVariable("boardId") Long boardId,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        adminService.deletePost(boardId);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 삭제에 성공했습니다", null), HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     *
     * @param boardId          게시판PK
     * @param commentId        댓글PK
     */
    @DeleteMapping("/admin/board/{boardId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("boardId") Long boardId,
                                           @PathVariable("commentId") Long commentId) {

        adminService.deleteComment(boardId, commentId);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 작성이 완료되었습니다", null), HttpStatus.CREATED);
    }

}
