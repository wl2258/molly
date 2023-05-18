package kr.co.kumoh.illdang100.mollyspring.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CommentRespDto {

    @AllArgsConstructor
    @Getter @Setter
    public static class CreateCommentResponse{

        private Long commentId;
    }
}
