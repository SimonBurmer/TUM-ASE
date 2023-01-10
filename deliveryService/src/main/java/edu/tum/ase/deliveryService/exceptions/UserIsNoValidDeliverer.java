package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="User is no valid deliverer")
public class UserIsNoValidDeliverer extends RuntimeException{
}
