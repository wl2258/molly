package kr.co.kumoh.illdang100.mollyspring.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public class BoardRespDto {

    @AllArgsConstructor
    @Getter @Setter
    public static class CreatePostResponse {
        private Long boardId;
    }

    @Getter @Setter
    public static class SearchPostListResult {
        Page<SearchPostListDto> result;
    }

    @AllArgsConstructor
    @Getter @Setter
    public static class SearchPostListDto {
        private String title;
        private String writerNick;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        private String content;
        private long views;
        private long commentCount;
        private long likyCount;
        private boolean hasImage;
        private boolean isNotice;
    }
}
