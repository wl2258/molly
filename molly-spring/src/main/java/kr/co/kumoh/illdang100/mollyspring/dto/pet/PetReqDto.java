package kr.co.kumoh.illdang100.mollyspring.dto.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetGenderEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class PetReqDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class PetSaveRequest {
        @NotNull
        private Long userId;

        @NotNull
        private PetTypeEnum petType;

        @NotBlank(message = "반려동물 이름은 빈 문자열이면 안 됩니다.")
        @Size(max = 20)
        private String petName;

        @NotBlank(message = "반려동물 품종은 빈 문자열이면 안 됩니다.")
        private String species;

        @NotNull
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate birthdate;

        @NotNull
        private PetGenderEnum gender;

        private boolean neuteredStatus;

        @DecimalMin(value = "0.0")
        private double weight;

        private MultipartFile petProfileImage;

        @Size(min = 1, max = 100)
        private String caution;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PetUpdateRequest {
        @NotNull
        private Long userId;

        @NotNull
        private Long petId;

        @NotNull
        private PetTypeEnum petType;

        @NotBlank(message = "반려동물 이름은 빈 문자열이면 안 됩니다.")
        private String petName;

        @NotBlank(message = "반려동물 품종은 빈 문자열이면 안 됩니다.")
        private String species;

        @NotNull
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate birthdate;

        @NotNull
        private PetGenderEnum gender;

        private boolean neuteredStatus;

        @DecimalMin(value = "0.0")
        private double weight;

        @Size(min = 1, max = 100)
        private String caution;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PetProfileUpdateRequest {
        @NotNull
        private Long petId;
        @NotNull
        private MultipartFile petProfileImage;
    }
}
