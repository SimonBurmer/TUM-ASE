package edu.tum.ase.authService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Cannot delete own user")
public class UserSelfDeletionException  extends RuntimeException{
}
