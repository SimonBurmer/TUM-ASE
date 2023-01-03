package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Delivery is not part of this box")
public class DeliveryNotPartOfBoxException extends RuntimeException{
}
