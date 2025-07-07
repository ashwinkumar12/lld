package com.ashwinkumar.lld.lockermanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Contact {

    private String phone;
    private String email;

    /**
     * Checks if the phone number is non-null and not empty after trimming whitespace.
     *
     * @return true if the phone number is present and not blank; false otherwise
     */
    public boolean hasValidPhone() {
        return phone != null && !phone.trim().isEmpty();
    }

    /**
     * Checks if the email field is non-null, non-empty after trimming, and contains an '@' character.
     *
     * @return true if the email is valid; false otherwise
     */
    public boolean hasValidEmail() {
        return email != null && !email.trim().isEmpty() && email.contains("@");
    }

    /**
     * Checks if both the phone and email fields are valid.
     *
     * @return {@code true} if the contact has a valid phone number and a valid email address; {@code false} otherwise.
     */
    public boolean isComplete() {
        return hasValidPhone() && hasValidEmail();
    }

    /**
     * Returns a string representation of the contact if both phone and email are valid; otherwise, returns an empty string.
     *
     * @return a formatted string with phone and email if the contact is complete, or an empty string if incomplete
     */
    @Override
    public String toString() {
        if (isComplete()) {
            return "Contact{phone='" + phone + "', email='" + email + "'}";
        } else {
            return "";
        }
    }

}
