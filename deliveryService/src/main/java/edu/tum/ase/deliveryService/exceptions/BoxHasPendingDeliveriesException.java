package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Box has not yet delivered deliveries!")
public class BoxHasPendingDeliveriesException extends RuntimeException{
}
