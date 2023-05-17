package kr.co.kumoh.illdang100.mollyspring.dto.comment;

import lombok.Getter;
import lombok.Setter;

public class CommentReqDto {

    @Getter @Setter
    public static class CreateCommentRequest {

        private String commentContent;
    }
}
