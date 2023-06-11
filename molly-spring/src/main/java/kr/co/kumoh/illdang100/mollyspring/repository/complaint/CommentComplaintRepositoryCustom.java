package kr.co.kumoh.illdang100.mollyspring.repository.complaint;

import kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentComplaintRepositoryCustom {

    Slice<AdminRespDto.RetrieveComplaintListDto> searchSlice(Pageable pageable);
}
