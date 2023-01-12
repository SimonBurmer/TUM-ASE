package edu.tum.ase.authService.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class BoxValidator implements ConstraintValidator<BoxValidationRule, String> {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest currentRequest;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpHeaders headers = new HttpHeaders();
        String header = currentRequest.getHeader(HttpHeaders.COOKIE);
        headers.set(HttpHeaders.COOKIE, header);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange("lb://DELIVERY-SERVICE/box/" + value, HttpMethod.GET, entity, Object.class);
            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null;
        } catch (HttpClientErrorException e) {
            return false;
        }
    }
}
