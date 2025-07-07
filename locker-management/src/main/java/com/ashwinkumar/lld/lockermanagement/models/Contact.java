package com.ashwinkumar.lld.lockermanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Contact {

    private String phone;
    private String email;

    public boolean hasValidPhone() {
        return phone != null && !phone.trim().isEmpty();
    }

    public boolean hasValidEmail() {
        return email != null && !email.trim().isEmpty() && email.contains("@");
    }

    public boolean isComplete() {
        return hasValidPhone() && hasValidEmail();
    }

    @Override
    public String toString() {
        return "Contact{phone='" + phone + "', email='" + email + "'}";
    }

}
