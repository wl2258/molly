package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.dto.surgery.SurgeryRespDto;
import kr.co.kumoh.illdang100.mollyspring.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import static kr.co.kumoh.illdang100.mollyspring.dto.surgery.SurgeryReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.surgery.SurgeryRespDto.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/pet/surgery")
@RequiredArgsConstructor
public class SurgeryApiController {

    private final SurgeryService surgeryService;

    @PostMapping
    public ResponseEntity<?> saveSurgery(@RequestBody @Valid SurgerySaveRequest surgerySaveRequest, BindingResult bindingResult) {

        surgeryService.saveSurgery(surgerySaveRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "수술 이력 저장 성공", null), HttpStatus.CREATED);
    }

    @PostMapping("{petId}")
    public ResponseEntity<?> updateSurgery(@PathVariable Long petId, @RequestBody @Valid SurgeryUpdateRequest surgeryUpdateRequest, BindingResult bindingResult) {

        surgeryService.updateSurgery(petId, surgeryUpdateRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "수술 이력 수정 성공", null), HttpStatus.OK);
    }

    @GetMapping("{petId}")
    public ResponseEntity<?> viewSurgery(@PathVariable @NotNull Long petId) {

        List<SurgeryResponse> surgeryResponses = surgeryService.viewSurgeryList(petId);

        return  new ResponseEntity<>(new ResponseDto<>(1, "수술 이력 조회 성공", surgeryResponses), HttpStatus.OK);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteSurgery(@RequestBody @Valid SurgeryDeleteRequest surgeryDeleteRequest, BindingResult bindingResult) {

        surgeryService.deleteSurgery(surgeryDeleteRequest);

        return new ResponseEntity<>(new ResponseDto<>(1, "수술 이력 삭제 성공", null), HttpStatus.OK);
    }
}
