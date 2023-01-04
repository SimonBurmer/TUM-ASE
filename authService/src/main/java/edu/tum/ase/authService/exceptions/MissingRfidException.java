package edu.tum.ase.authService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Such a user requires a rfid token")
public class MissingRfidException extends RuntimeException {
}
