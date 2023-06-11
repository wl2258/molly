package kr.co.kumoh.illdang100.mollyspring.dto.suspension;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SuspensionReqDto {

    @AllArgsConstructor
    @Getter
    public static class SuspendAccountRequest {

        @NotNull
        @Min(value = 1, message = "suspensionPeriod는 1보다 커야 합니다.")
        private Long suspensionPeriod;
        @NotBlank
        @Pattern(regexp = "(SPAM_PROMOTION|PORNOGRAPHY|ILLEGAL_INFORMATION|HARMFUL_TO_MINORS|OFFENSIVE_EXPRESSION|PERSONAL_INFORMATION_EXPOSURE|UNPLEASANT_EXPRESSION|ANIMAL_CRUELTY|FAKE_INFORMATION)$")
        private String reason;
    }
}
