package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetRespDto.PetDetailResponse;
import kr.co.kumoh.illdang100.mollyspring.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static kr.co.kumoh.illdang100.mollyspring.dto.pet.PetReqDto.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/pet")
@RequiredArgsConstructor
public class PetApiController {

    private final PetService petService;
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerPet(@ModelAttribute @Valid PetSaveRequest petSaveRequest, BindingResult bindingResult) throws IOException {

        PetDetailResponse petDetailResponse = petService.registerPet(petSaveRequest);

        return new ResponseEntity<>(new ResponseDto(1, "반려동물 등록을 성공했습니다.", petDetailResponse), HttpStatus.CREATED);
    }
    @GetMapping("{petId}")
    public ResponseEntity<?> viewDetails(@PathVariable Long petId) {

        PetDetailResponse petDetailResponse = petService.viewDetails(petId);

        return new ResponseEntity<>(new ResponseDto(1, "해당 반려동물의 정보입니다.", petDetailResponse), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<?> updatePet(@ModelAttribute @Valid PetUpdateRequest petUpdateRequest, BindingResult bindingResult) {

        petService.updatePet(petUpdateRequest);

        return new ResponseEntity<>(new ResponseDto(1, "반려동물 정보 수정을 성공했습니다.", null), HttpStatus.OK);
    }

    @DeleteMapping("{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {

        return new ResponseEntity<>(new ResponseDto(1, "반려동물 삭제를 성공했습니다.", null), HttpStatus.OK);
    }


    @PutMapping(path = "/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updatePetProfile(Long petId, @ModelAttribute MultipartFile profileImage) throws IOException {

        petService.updatePetProfileFile(petId, profileImage);

        return new ResponseEntity<>(new ResponseDto(1, "반려동물 프로필 이미지를 수정했습니다.", null), HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deletePetProfile(Long petId) {

        petService.deletePetProfileFile(petId);

        return new ResponseEntity<>(new ResponseDto(1, "반려동물 프로필 이미지를 삭제했습니다.", null), HttpStatus.OK);
    }
}
