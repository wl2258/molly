package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetRespDto.PetDetailResponse;
import kr.co.kumoh.illdang100.mollyspring.service.ImageFileService;
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
    private final ImageFileService imageFileService;
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerPet(@ModelAttribute @Valid PetSaveRequest request, BindingResult bindingResult) throws IOException {

        PetDetailResponse response = petService.registerPet(request);

        return new ResponseEntity<>(new ResponseDto(1, "", response), HttpStatus.CREATED);
    }
    @GetMapping("{petId}")
    public ResponseEntity<?> viewDetails(@PathVariable Long petId) {

        PetDetailResponse response = petService.viewDetails(petId);

        return new ResponseEntity<>(new ResponseDto(1, "", response), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<?> updatePet(@ModelAttribute @Valid PetUpdateRequest request, BindingResult bindingResult) {

        petService.updatePet(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(path = "/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updatePetProfile(Long petId, @ModelAttribute MultipartFile profileImage) throws IOException {

        imageFileService.updatePetProfileFile(petId, profileImage);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deletePetProfile(Long petId) {

        imageFileService.deletePetProfileFile(petId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
