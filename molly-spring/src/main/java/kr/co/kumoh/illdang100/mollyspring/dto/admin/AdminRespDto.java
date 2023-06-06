package kr.co.kumoh.illdang100.mollyspring.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class AdminRespDto {

    @AllArgsConstructor
    @Getter
    public static class RetrieveComplaintListDto {
        private Long complaintId;
        private String reporterEmail;
        private String reportedEmail;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class ComplaintDetailResponse {
        private Long complaintId;
        private String reporterEmail;
        private String reportedEmail;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        private String reason;
    }
}
