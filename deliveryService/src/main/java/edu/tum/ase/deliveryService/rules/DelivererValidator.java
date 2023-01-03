package edu.tum.ase.deliveryService.rules;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DelivererValidator implements ConstraintValidator<DelivererValidationRule, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO: check if given value is registered as a customer
        return true;
    }
}
