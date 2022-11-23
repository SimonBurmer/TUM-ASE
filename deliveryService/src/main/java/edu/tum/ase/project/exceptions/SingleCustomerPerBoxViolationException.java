package edu.tum.ase.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="This box has already some deliveries for a different customer assigned.")
public class SingleCustomerPerBoxViolationException extends RuntimeException {
}
