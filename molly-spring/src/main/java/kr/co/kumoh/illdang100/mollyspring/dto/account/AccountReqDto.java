package kr.co.kumoh.illdang100.mollyspring.dto.account;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class AccountReqDto {

    @Data
    public static class InputNicknameRequest {

        @NotEmpty
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z]{1,10}$", message = "한글/영문 1~10자 이내로 작성해주세요")
        private String nickname;
    }

    @Data
    public static class SaveAccountRequest {

        @NotEmpty
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z]{1,10}$", message = "한글/영문 1~10자 이내로 작성해주세요")
        private String nickname;

        private MultipartFile accountProfileImage;
    }

}
