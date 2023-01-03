package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Cannot perform this action given the status of the delivery")
public class DeliveryStatusException extends RuntimeException{
}
