package edu.tum.ase.deliveryService.rules;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({FIELD})
@Constraint(validatedBy = DelivererValidator.class)
public @interface DelivererValidationRule {
    String message() default "{validator.propertypatern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
