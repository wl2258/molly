package kr.co.kumoh.illdang100.mollyspring.dto.surgery;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class SurgeryReqDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgerySaveRequest {

        @NotNull
        private Long petId;

        @NotBlank(message = "수술명은 빈 문자열이면 안됩니다.")
        private String surgeryName;

        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "수술 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate surgeryDate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgeryUpdateRequest {

        @NotNull
        private Long petId;

        @NotNull
        private Long surgeryId;

        @NotBlank(message = "수술명은 빈 문자열이면 안됩니다.")
        private String surgeryName;

        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "수술 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate surgeryDate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgeryDeleteRequest {

        @NotNull
        private Long petId;

        @NotNull
        private Long surgeryId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgeryRequest {

        @NotNull
        private Long petId;

        @NotBlank(message = "수술명은 빈 문자열이면 안됩니다.")
        private String surgeryName;

        @DateTimeFormat(pattern="yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "수술 날짜는 yyyy-MM-dd 형식이어야 합니다.")
        private LocalDate surgeryDate;
    }
}