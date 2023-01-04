package edu.tum.ase.authService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Such a user does already exist")
public class UserAlreadyExistsException extends RuntimeException {
}
