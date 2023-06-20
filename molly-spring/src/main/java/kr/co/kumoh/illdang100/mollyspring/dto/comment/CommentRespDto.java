package kr.co.kumoh.illdang100.mollyspring.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CommentRespDto {

    @AllArgsConstructor
    @Getter @Setter
    public static class CreateCommentResponse{

        private Long commentId;
        private boolean commentOwner;
        private String commentAccountEmail;
        private String commentWriterNick;
        private LocalDateTime commentCreatedAt;
        private String content;
        private String commentProfileImage;
    }
}
