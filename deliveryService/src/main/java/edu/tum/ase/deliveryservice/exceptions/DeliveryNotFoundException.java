package edu.tum.ase.deliveryservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Delivery not found")
public class DeliveryNotFoundException extends RuntimeException {
}
