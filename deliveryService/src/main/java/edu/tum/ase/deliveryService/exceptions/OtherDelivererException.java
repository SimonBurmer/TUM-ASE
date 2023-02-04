package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Cannot pick up this delivery, because it is assigned to a different deliverer")
public class OtherDelivererException extends RuntimeException{
}
