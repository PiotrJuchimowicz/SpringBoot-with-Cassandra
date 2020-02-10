package com.company.project.unit;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MailValidatorTest {

    @Test
    @DisplayName("Valid mail should not break constraints")
    public void validMailTest() {
        //given
        String validMail = "user@domain.com";

        //when
       EmailValidator emailValidator = new EmailValidator();
       boolean isEmailValid = emailValidator.isValid(validMail, null);

        //then
        assertTrue(isEmailValid);
    }

    @Test
    @DisplayName("Invalid mail should break constraints")
    public void invalidMailTest() {
        //given
        String invalidMail = "13123";

        //when
       EmailValidator emailValidator = new EmailValidator();
       boolean isEmailValid = emailValidator.isValid(invalidMail, null);

        //then
        assertFalse(isEmailValid);
    }
}
