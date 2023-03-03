package com.chuwa.redbook.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EitherOrValidator.class})
public @interface EitherOr {
    String message() default "A post can only be linked to a video or a list of pictures.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}