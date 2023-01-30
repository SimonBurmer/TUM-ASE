package edu.tum.ase.emailservice.service;

import edu.tum.ase.backendCommon.model.AseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EMailAddressService {

    @Autowired
    private RestTemplate restTemplate;

    public String getEMailAddressByUserId(String userId) {
        ResponseEntity<AseUser> response = this.restTemplate.exchange("lb://AUTH-SERVICE/intern/user/" + userId,
                HttpMethod.GET, null, AseUser.class);
        return response.getBody().getEmail();
    }
}
