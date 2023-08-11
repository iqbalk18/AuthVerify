package com.signup.auth.authentication2.phone;

import com.signup.auth.authentication2.model.RegisterRequestPhone;

public interface SmsSender {

    void sendSms(RegisterRequestPhone registerRequestPhone);

    // or maybe void sendSms(String phoneNumber, String message);
}
