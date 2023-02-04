package edu.tum.ase.authService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="A user with this email does already exist")
public class UserAlreadyExistsException extends RuntimeException {
}
