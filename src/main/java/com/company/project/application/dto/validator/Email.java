package com.company.project.application.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.FIELD, ElementType.PARAMETER})
@Retention( RetentionPolicy.RUNTIME)
@Constraint( validatedBy = EmailValidator.class)
public @interface Email {
    String message() default "The email format must be correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
