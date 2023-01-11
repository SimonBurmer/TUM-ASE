package edu.tum.ase.deliveryService.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerValidator implements ConstraintValidator<CustomerValidationRule, String> {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest currentRequest;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpHeaders headers = new HttpHeaders();
        String header = currentRequest.getHeader(HttpHeaders.COOKIE);
        headers.set(HttpHeaders.COOKIE, header);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange("lb://AUTH-SERVICE/user/is_customer/" + value, HttpMethod.GET, entity, Boolean.class);
            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody();
        } catch (HttpClientErrorException e) {
            return false;
        }
    }
}
