package kr.co.kumoh.illdang100.mollyspring.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        private Long reportedItemId;
        private String reporterEmail;
        private String reportedEmail;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        private String reason;
    }

    @AllArgsConstructor
    @Builder
    @Getter @Setter
    public static class PostDetailForAdminResponse {
        private String title;
        private String category;
        private String petType;
        private String content;
        private String writerNick;
        private String writerEmail;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        private long views;
        private String writerProfileImage;
        private List<BoardCommentForAdminDto> comments;
        private boolean thumbsUp;
        private long likyCnt;
    }

    @AllArgsConstructor
    @Getter @Setter
    public static class BoardCommentForAdminDto {
        private Long commentId;
        private String commentAccountEmail;
        private String commentWriteNick;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime commentCreatedAt;
        private String content;
        private String commentProfileImage;
    }
}
