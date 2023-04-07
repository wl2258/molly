package kr.co.kumoh.illdang100.mollyspring.dto.medication;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class MedicationReqDto {

    @Data
    @AllArgsConstructor
    public static class MedicationSaveRequest {

        @NotNull
        private Long petId;

        @NotBlank(message = "복용약 이름은 빈 문자열이면 안됩니다.")
        private String medicationName;

        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "복용약 시작 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate medicationStartDate;

        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "복용약 종료 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate medicationEndDate;
    }

    @Data
    @AllArgsConstructor
    public static class MedicationUpdateRequest {
        @NotNull
        private Long petId;

        @NotNull
        private Long medicationId;

        @NotBlank(message = "복용약 이름은 빈 문자열이면 안 됩니다.")
        private String medicationName;

        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "복용약 시작 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate medicationStartDate;

        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "복용약 종료 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate medicationEndDate;
    }

    @Data
    @AllArgsConstructor
    public static class MedicationDeleteRequest {
        @NotNull
        private Long petId;

        @NotNull
        private Long medicationId;
    }
}