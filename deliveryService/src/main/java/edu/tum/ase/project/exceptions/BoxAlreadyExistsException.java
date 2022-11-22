package edu.tum.ase.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Such a box does already exist")
public class BoxAlreadyExistsException extends RuntimeException{
}
