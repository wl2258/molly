package kr.co.kumoh.illdang100.mollyspring.dto.surgery;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SurgeryReqDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgerySaveRequest {
        private Long petId;
        private String surgeryName;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate surgeryDate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgeryUpdateRequest {
        private Long petId;
        private Long surgeryId;
        private String surgeryName;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate surgeryDate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgeryDeleteRequest {
        private Long petId;
        private Long surgeryId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SurgeryRequest {
        private Long petId;
        private String surgeryName;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate surgeryDate;
    }
}