package kr.co.kumoh.illdang100.mollyspring.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class BoardReqDto {

    @Getter
    public static class CreatePostRequest {

        @NotBlank
        @Size(max = 60, message = "제목은 60자 이하로 작성해주세요")
        private String title;
        @NotBlank
        @Size(max = 5000, message = "내용은 5,000자 이하로 작성해주세요")
        private String content;
        @NotBlank
        @Pattern(regexp = "(MEDICAL|FREE)$")
        private String category;
        @NotBlank
        @Pattern(regexp = "(CAT|DOG|RABBIT|NOT_SELECTED)$")
        private String petType;
        private List<Long> boardImageIds;
    }

    @Getter
    public static class QuitCreatePostRequest {
        private List<Long> boardImageIds;
    }

    @Getter
    public static class UpdatePostRequest {

        @NotBlank
        @Size(max = 60, message = "제목은 60자 이하로 작성해주세요")
        private String title;
        @NotBlank
        @Size(max = 5000, message = "내용은 5,000자 이하로 작성해주세요")
        private String content;
        @NotBlank
        @Pattern(regexp = "(MEDICAL|FREE)$")
        private String category;
        @NotBlank
        @Pattern(regexp = "(CAT|DOG|RABBIT|NOT_SELECTED)$")
        private String petType;
    }

    @AllArgsConstructor
    @Getter
    public static class RetrievePostListCondition {
        @NotBlank
        @Pattern(regexp = "(MEDICAL|FREE|ALL)$")
        private String category;
        @NotBlank
        @Pattern(regexp = "(CAT|DOG|RABBIT|ALL)$")
        private String petType;
        @Nullable
        private String searchWord;
    }
}
