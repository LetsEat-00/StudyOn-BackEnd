package com.LetsEat.StudyOn.domain.email.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.domain.email.dto.EmailRequestDto;
import com.LetsEat.StudyOn.domain.email.dto.EmailVerifyDto;
import com.LetsEat.StudyOn.domain.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.LetsEat.StudyOn.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    /**
     * 인증 메일 발송
     * @param emailRequestDto 메일 발송할 이메일
     * @return 발송 성공 응답 메시지
     */
    @PostMapping("/email/send")
    public ResponseEntity<CommonResponse<?>> sendEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        emailService.sendEmail(emailRequestDto.getEmail());
        return getResponseEntity("인증 메일 발송 성공");
    }

    /**
     * 이메일 인증
     * @param emailVerifyDto 이메일, 인증코드
     * @return 인증 성공 응답 메시지
     */
    @PatchMapping("/email/verify")
    public ResponseEntity<CommonResponse<?>> verifyEmail(@Valid @RequestBody EmailVerifyDto emailVerifyDto) {
        emailService.verifyEmail(emailVerifyDto);
        return getResponseEntity("이메일 인증 성공");
    }
}
