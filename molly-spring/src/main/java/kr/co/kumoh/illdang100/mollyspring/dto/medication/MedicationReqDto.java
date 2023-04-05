package kr.co.kumoh.illdang100.mollyspring.dto.medication;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class MedicationReqDto {

    @Data
    @AllArgsConstructor
    public static class MedicationSaveRequest {
        private Long petId;
        private String medicationName;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate medicationStartDate;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate medicationEndDate;
    }

    @Data
    @AllArgsConstructor
    public static class MedicationUpdateRequest {
        private Long petId;
        private Long medicationId;
        private String medicationName;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate medicationStartDate;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate medicationEndDate;
    }

    @Data
    @AllArgsConstructor
    public static class MedicationDeleteRequest {
        private Long petId;
        private Long medicationId;
    }

    @Data
    @AllArgsConstructor
    public static class MedicationRequest {
        private String medicationName;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate medicationStartDate;
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate medicationEndDate;
    }
}