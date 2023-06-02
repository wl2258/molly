package kr.co.kumoh.illdang100.mollyspring.dto.complaint;

import lombok.Getter;

import javax.validation.constraints.*;

public class ComplaintReqDto {

    @Getter
    public static class ReportRequest {
        @NotBlank
        @Pattern(regexp = "(SPAM_PROMOTION|PORNOGRAPHY|ILLEGAL_INFORMATION|HARMFUL_TO_MINORS|OFFENSIVE_EXPRESSION|PERSONAL_INFORMATION_EXPOSURE|UNPLEASANT_EXPRESSION|ANIMAL_CRUELTY|FAKE_INFORMATION)$")
        private String reason;
    }
}
