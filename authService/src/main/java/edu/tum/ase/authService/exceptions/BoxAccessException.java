package edu.tum.ase.authService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="This user is currently not authorized to open this box")
public class BoxAccessException extends RuntimeException {
}
