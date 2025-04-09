package com.example.generateOTP.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SmsController {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.trial_number}")
    private String trialNumber;

    Map<String, String> map = new HashMap<>();

    @GetMapping("/otp/{num}")
    public ResponseEntity getSms(@PathVariable String num) {
        Twilio.init(accountSid, authToken);
        Message.creator(new PhoneNumber(num), new PhoneNumber(trialNumber), generateOTP(num)).create();
        return new ResponseEntity("message sent", HttpStatus.OK);
    }

    private String generateOTP(String num) {
        String otp = "" + (int) (Math.random() * 1000000);
        map.put(num, otp);
        return otp;
    }

    @GetMapping("/validation/{num}/{otp}")
    public ResponseEntity validateOTP(@PathVariable String num, @PathVariable String otp) {
        if (map.containsKey(num)) {
            if (otp.equals(map.get(num))) {
                return new ResponseEntity("OTP validation success", HttpStatus.OK);
            }
        }
        return new ResponseEntity("Invalid OTP", HttpStatus.BAD_REQUEST);
    }
}
