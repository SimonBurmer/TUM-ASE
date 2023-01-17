package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="A delivery that has been picked up, is in the target box or has been delivered cannot be modified or reassigned anymore")
public class DeliveryModificationNotAllowedException extends RuntimeException{
}
