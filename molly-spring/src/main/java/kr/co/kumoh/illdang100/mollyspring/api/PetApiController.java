package kr.co.kumoh.illdang100.mollyspring.api;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetAddRequest;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetAddResponse;
import kr.co.kumoh.illdang100.mollyspring.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/auth/pet")
@RequiredArgsConstructor
public class PetApiController {

    private final PetService petService;
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerPet(@ModelAttribute @Valid PetAddRequest request, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ResponseDto(-1 , "애완동물 등록 실패", null), HttpStatus.BAD_REQUEST);
        }

        Long petId = petService.registerPet(request, request.getImageFile());

        if (petId == null) {
            return new ResponseEntity<>(new ResponseDto(-1, "애완동물 등록 실패", null), HttpStatus.FOUND);
        }

        return new ResponseEntity<>(new ResponseDto(1, "", new PetAddResponse(petId)), HttpStatus.CREATED);
    }

}
