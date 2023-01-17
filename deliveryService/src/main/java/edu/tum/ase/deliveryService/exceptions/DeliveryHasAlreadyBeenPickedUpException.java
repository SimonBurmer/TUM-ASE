package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="This delivery has already been picked up")
public class DeliveryHasAlreadyBeenPickedUpException extends RuntimeException{
}
