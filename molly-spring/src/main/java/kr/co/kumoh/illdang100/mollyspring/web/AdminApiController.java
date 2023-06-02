package kr.co.kumoh.illdang100.mollyspring.web;

import kr.co.kumoh.illdang100.mollyspring.dto.ResponseDto;
import kr.co.kumoh.illdang100.mollyspring.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.kumoh.illdang100.mollyspring.dto.suspension.SuspensionReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AdminApiController {

    private final AdminService adminService;

    @PostMapping("/admin/account/{accountId}/suspend/board/{boardId}")
    public ResponseEntity<?> suspendAccountByBoard(@PathVariable("accountId") Long accountId,
                                                   @PathVariable("boardId") Long boardId,
                                                   @RequestBody SuspendAccountRequest suspendAccountRequest) {

        adminService.suspendAccount(accountId, boardId, null, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }

    @PostMapping("/admin/account/{accountId}/suspend/comment/{commentId}")
    public ResponseEntity<?> suspendAccountByComment(@PathVariable("accountId") Long accountId,
                                                     @PathVariable("commentId") Long commentId,
                                                     @RequestBody SuspendAccountRequest suspendAccountRequest) {

        adminService.suspendAccount(accountId, null, commentId, suspendAccountRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "정지되었습니다", null), HttpStatus.OK);
    }
}
