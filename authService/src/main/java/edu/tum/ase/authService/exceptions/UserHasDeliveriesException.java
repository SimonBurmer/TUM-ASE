package edu.tum.ase.authService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="This user stil has some deliveries associated. Please delete those first")
public class UserHasDeliveriesException extends RuntimeException{
}
