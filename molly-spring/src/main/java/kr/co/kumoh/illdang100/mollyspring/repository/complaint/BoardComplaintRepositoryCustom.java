package kr.co.kumoh.illdang100.mollyspring.repository.complaint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto.*;

public interface BoardComplaintRepositoryCustom {

    Slice<RetrieveComplaintListDto> searchSlice(Pageable pageable);
}
