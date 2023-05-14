package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.service.BoardService;
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

import javax.validation.Valid;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class BoardApiController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     *
     * @param createPostRequest 게시글 정보
     * @param principalDetails  인증된 사용자 정보
     * @return 저장된 게시글 PK
     */
    @PostMapping("/auth/board")
    public ResponseEntity<?> createNewPost(@ModelAttribute @Valid CreatePostRequest createPostRequest,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CreatePostResponse createPostResponse = boardService.createPost(principalDetails.getAccount().getId(), createPostRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 작성에 성공했습니다", createPostResponse), HttpStatus.OK);
    }

    @PutMapping("/auth/board/{boardId}")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal PrincipalDetails principalDetails) {

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

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> retrievePostDetail(@PathVariable("boardId") Long boardId,
                                                @RequestHeader(value = "AccountId", required = false) Long accountId) {

        PostDetailResponse postDetailResponse = boardService.getPostDetail(boardId, accountId);
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 상세 조회에 성공했습니다", postDetailResponse), HttpStatus.OK);
    }

    @DeleteMapping("/auth/board/{boardId}")
    public ResponseEntity<?> deletePost(@PathVariable("boardId") Long boardId,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boardService.deletePost(boardId, principalDetails.getAccount().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 삭제에 성공했습니다", null), HttpStatus.NO_CONTENT);
    }
}
