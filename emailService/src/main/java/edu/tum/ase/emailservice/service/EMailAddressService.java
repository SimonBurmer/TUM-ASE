package edu.tum.ase.emailservice.service;

import edu.tum.ase.backendCommon.model.AseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EMailAddressService {

    @Autowired
    private RestTemplate restTemplate;

    public String getEMailAddressByUserId(String headerField, String headerContent, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerField, headerContent);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = this.restTemplate.exchange("lb://AUTH-SERVICE/user/" + userId,
                HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                });
        return ((AseUser) response.getBody()).getEmail();
    }
}
