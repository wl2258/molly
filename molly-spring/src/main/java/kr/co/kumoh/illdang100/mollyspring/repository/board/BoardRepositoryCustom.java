package kr.co.kumoh.illdang100.mollyspring.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;

public interface BoardRepositoryCustom {

    Page<RetrievePostListDto> findPagePostList(RetrievePostListCondition retrievePostListCondition, Pageable pageable);
}
