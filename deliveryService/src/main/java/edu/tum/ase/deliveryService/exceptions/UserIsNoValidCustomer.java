package edu.tum.ase.deliveryService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="User is no valid customer")
public class UserIsNoValidCustomer extends RuntimeException{
}
