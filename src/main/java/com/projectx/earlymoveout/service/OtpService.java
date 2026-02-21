package com.projectx.earlymoveout.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {

    private final SecureRandom random = new SecureRandom();

    public String generate5DigitOtp() {
        int n = random.nextInt(100000); // 0..99999
        return String.format("%05d", n); // always 5 digits
    }
}
