package com.shoppie.controllers;

import com.shoppie.globals.ApiResponse;
import com.shoppie.payloads.otp.OtpResendRequest;
import com.shoppie.payloads.otp.OtpVerifyRequest;
import com.shoppie.services.OtpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/otp")
@Tag(name = "OtpController", description = "OTP features for Shoppie")
public class OtpController {
    private final OtpService service;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        service.verify(request);
        return new ResponseEntity<>(ApiResponse.success("Account verified successfully", null), HttpStatus.OK);
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody OtpResendRequest request) {
        service.resend(request);
        return new ResponseEntity<>(ApiResponse.success("OTP sent successfully", null), HttpStatus.OK);
    }
}
