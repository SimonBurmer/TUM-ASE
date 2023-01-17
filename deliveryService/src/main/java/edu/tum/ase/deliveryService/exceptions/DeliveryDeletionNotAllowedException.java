package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="A delivery that has been picked up or is in the target box cannot be deleted")
public class DeliveryDeletionNotAllowedException extends RuntimeException{
}
