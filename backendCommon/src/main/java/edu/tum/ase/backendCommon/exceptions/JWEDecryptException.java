package edu.tum.ase.backendCommon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Failed to decrypt a JWE")
public class JWEDecryptException extends RuntimeException{
}
