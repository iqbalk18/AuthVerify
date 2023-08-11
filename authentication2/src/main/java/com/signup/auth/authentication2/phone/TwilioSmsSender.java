package com.signup.auth.authentication2.phone;

import com.signup.auth.authentication2.model.RegisterRequestPhone;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("twilio")
public class TwilioSmsSender implements SmsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsSender.class);

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void sendSms(RegisterRequestPhone registerRequestPhone) {
        if (isPhoneNumberValid(registerRequestPhone.getPhone())) {
            PhoneNumber to = new PhoneNumber(registerRequestPhone.getPhone());
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
            String message = registerRequestPhone.getMessage();
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            LOGGER.info("Send sms {}", registerRequestPhone);
        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + registerRequestPhone.getPhone() + "] is not a valid number"
            );
        }

    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // TODO: Implement phone number validator
        return true;
    }
}
