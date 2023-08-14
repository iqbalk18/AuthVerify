package com.signup.auth.authentication2.model.registermodel;

import com.signup.auth.authentication2.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestPhone {
    private String firstname;
    private String lastname;
    private String phone;
    private String password;
    private Role role;
    private String message;
    @Override
    public String toString() {
        return "SmsRequest{" +
                "phoneNumber= ..." + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
