package com.signup.auth.authentication2.phone.sms;

import com.signup.auth.authentication2.model.registermodel.RegisterRequestPhone;

public interface SmsSender {

    void sendSms(RegisterRequestPhone registerRequestPhone);

    // or maybe void sendSms(String phoneNumber, String message);
}
