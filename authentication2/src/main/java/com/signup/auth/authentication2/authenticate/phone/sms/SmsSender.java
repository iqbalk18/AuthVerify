package com.signup.auth.authentication2.authenticate.phone.sms;

import com.signup.auth.authentication2.authenticate.model.registermodel.RegisterRequestPhone;

public interface SmsSender {

    void sendSms(RegisterRequestPhone registerRequestPhone);

    // or maybe void sendSms(String phoneNumber, String message);
}
