package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetRespDto.PetHomeResponse;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/home")
@RequiredArgsConstructor
public class HomeApiController {
    private final PetService petService;
    @GetMapping
    public ResponseEntity<?> getHome(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        PetHomeResponse homeResponse = petService.getHome(principalDetails.getAccount().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "홈화면 정보", homeResponse), HttpStatus.OK);
    }
}
