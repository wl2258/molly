package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.service.BoardService;
import kr.co.kumoh.illdang100.mollyspring.service.CommentService;
import kr.co.kumoh.illdang100.mollyspring.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.comment.CommentReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.comment.CommentRespDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.complaint.ComplaintReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class BoardApiController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final ComplaintService complaintService;

    /**
     * 게시글 작성
     *
     * @param createPostRequest 게시글 정보
     * @param principalDetails  인증된 사용자 정보
     * @return 저장된 게시글 PK
     */
    @PostMapping("/auth/board")
    public ResponseEntity<?> createNewPost(@RequestBody @Valid CreatePostRequest createPostRequest,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CreatePostResponse createPostResponse = boardService.createPost(principalDetails.getAccount().getId(), createPostRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 작성에 성공했습니다", createPostResponse), HttpStatus.CREATED);
    }

    @PostMapping("/auth/board/quit")
    public ResponseEntity<?> quitCreatePost(@RequestBody QuitCreatePostRequest quitCreatePostRequest,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.quitCreatePost(quitCreatePostRequest.getBoardImageIds());
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 작성이 중단되었습니다", null), HttpStatus.OK);
    }

    /**
     * 게시글 수정
     *
     * @param boardId           게시글PK
     * @param updatePostRequest 수정한 게시글 정보
     * @param principalDetails  인증된 사용자 정보
     */
    @PostMapping("/auth/board/{boardId}")
    public ResponseEntity<?> editPost(@PathVariable("boardId") Long boardId,
                                      @RequestBody UpdatePostRequest updatePostRequest,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.updatePost(boardId, principalDetails.getAccount().getId(), updatePostRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정에 성공했습니다", null), HttpStatus.OK);
    }

    /**
     * 게시글 리스트 조회
     *
     * @param retrievePostListCondition 조회 조건, 검색어
     * @param pageable                  정렬 조건, 페이지, 사이즈
     * @return 게시글 리스트
     */
    @GetMapping("/board/list")
    public ResponseEntity<?> retrievePostList(@ModelAttribute @Valid RetrievePostListCondition retrievePostListCondition,
                                              BindingResult bindingResult,
                                              @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<RetrievePostListDto> postList = boardService.getPostList(retrievePostListCondition, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 리스트 조회에 성공했습니다", postList), HttpStatus.OK);
    }

    /**
     * 게시글 상세 조회
     *
     * @param boardId   게시글 PK
     * @param accountId 사용자 PK
     * @return 게시글 상세 정보(게시글, 게시글 이미지, 댓글, 좋아요)
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> retrievePostDetail(@PathVariable("boardId") Long boardId,
                                                @RequestHeader(value = "AccountId", required = false) Long accountId) {

        PostDetailResponse postDetailResponse = boardService.getPostDetail(boardId, accountId);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 상세 조회에 성공했습니다", postDetailResponse), HttpStatus.OK);
    }

    /**
     * 게시글 삭제
     *
     * @param boardId          삭제하고자 하는 게시글 PK
     * @param principalDetails 인증된 사용자 정보
     */
    @DeleteMapping("/auth/board/{boardId}")
    public ResponseEntity<?> deletePost(@PathVariable("boardId") Long boardId,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.deletePost(boardId, principalDetails.getAccount().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 삭제에 성공했습니다", null), HttpStatus.OK);
    }

    @PostMapping("/auth/board/image")
    public ResponseEntity<?> addBoardImage(@RequestParam("boardImage") MultipartFile boardImage,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // TODO: 이미지 크기 제한

        AddBoardImageResponse result = boardService.addBoardImage(principalDetails.getAccount().getId(), boardImage);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 이미지가 추가되었습니다", result), HttpStatus.CREATED);
    }

    /**
     * 새로운 댓글 작성
     *
     * @param boardId              게시글PK
     * @param createCommentRequest 댓글 내용
     * @param principalDetails     인증된 사용자 정보
     * @return 생성된 댓글PK
     */
    @PostMapping("/auth/board/{boardId}/comment")
    public ResponseEntity<?> createNewComment(@PathVariable("boardId") Long boardId,
                                              @RequestBody CreateCommentRequest createCommentRequest,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CreateCommentResponse result =
                commentService.createComment(createCommentRequest, principalDetails.getAccount().getId(), boardId);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 작성이 완료되었습니다", result), HttpStatus.CREATED);
    }

    /**
     * 댓글 삭제
     *
     * @param boardId          게시판PK
     * @param commentId        댓글PK
     * @param principalDetails 인증된 사용자 정보
     */
    @DeleteMapping("/auth/board/{boardId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("boardId") Long boardId,
                                           @PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        commentService.deleteComment(principalDetails.getAccount().getId(), boardId, commentId);
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 작성이 완료되었습니다", null), HttpStatus.CREATED);
    }

    /**
     * 게시글 좋아요 버튼 클릭
     *
     * @param boardId          게시글PK
     * @param principalDetails 인증된 사용자 정보
     * @return 게시글 좋아요 정보
     */
    @PostMapping("/auth/board/{boardId}/liky")
    public ResponseEntity<?> toggleLikePost(@PathVariable("boardId") Long boardId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        LikyBoardResponse result = boardService.toggleLikePost(principalDetails.getAccount().getId(), boardId);
        return new ResponseEntity<>(new ResponseDto<>(1, result.getMessage(), result), HttpStatus.OK);
    }

    @PostMapping("/auth/board/{boardId}/report")
    public ResponseEntity<?> reportPost(@PathVariable("boardId") Long boardId,
                                        @RequestBody ReportPostRequest reportPostRequest,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        complaintService.reportPost(boardId, principalDetails.getAccount().getId(), reportPostRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "신고가 정상적으로 이루어졌습니다", null), HttpStatus.CREATED);
    }
}
