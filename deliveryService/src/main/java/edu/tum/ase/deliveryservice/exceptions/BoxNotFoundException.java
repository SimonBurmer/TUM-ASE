package edu.tum.ase.deliveryservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Box not found")
public class BoxNotFoundException extends RuntimeException{
}
