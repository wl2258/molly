package kr.co.kumoh.illdang100.mollyspring.dto.vaccination;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class VaccinationReqDto {
    @Data
    @Builder
    @AllArgsConstructor
    public static class VaccinationSaveRequest {
        @NotNull
        private Long petId;

        @NotBlank(message = "예방접종 이름은 빈 문자열이면 안됩니다.")
        private String vaccinationName;
        
        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "예방접종 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate vaccinationDate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class VaccinationUpdateRequest {

        @NotNull
        private Long petId;

        @NotNull
        private Long vaccinationId;

        @NotBlank(message = "예방접종 이름은 빈 문자열이면 안됩니다.")
        private String vaccinationName;

        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "예방접종 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate vaccinationDate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class VaccinationDeleteRequest {

        @NotNull
        private Long petId;

        @NotNull
        private Long vaccinationId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class VaccinationRequest {

        @NotNull
        private Long petId;

        @NotBlank(message = "예방접종 이름은 빈 문자열이면 안됩니다.")
        private String vaccinationName;

        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "예방접종 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate vaccinationDate;
    }
}