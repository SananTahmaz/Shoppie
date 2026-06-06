package com.shoppie.services;

import com.shoppie.payloads.otp.OtpResendRequest;
import com.shoppie.payloads.otp.OtpVerifyRequest;

public interface OtpService {
    String generate();

    void verify(OtpVerifyRequest request);

    void resend(OtpResendRequest request);
}
