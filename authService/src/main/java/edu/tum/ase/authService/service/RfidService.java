package edu.tum.ase.authService.service;

import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RfidService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean checkCustomerAccess(HttpServletRequest currentRequest, AseUser customer) {
        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<Delivery> deliveries = fetchDeliveries(currentRequest, boxId);

        return deliveries.stream()
                .filter(d -> d.getStatus().equals(DeliveryStatus.IN_TARGET_BOX))
                .anyMatch(d -> d.getCustomer().equals(customer.getEmail()));
    }


    public boolean checkDelivererAccess(HttpServletRequest currentRequest, AseUser deliverer) {
        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<Delivery> deliveries = fetchDeliveries(currentRequest, boxId);

        return deliveries.stream()
                .filter(d -> d.getStatus().equals(DeliveryStatus.PICKED_UP))
                .anyMatch(d -> d.getDeliverer().equals(deliverer.getEmail()));
    }

    private List<Delivery> fetchDeliveries(HttpServletRequest currentRequest, String boxId) {
        HttpHeaders headers = new HttpHeaders();
        String header = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
        headers.set(HttpHeaders.AUTHORIZATION, header);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Delivery>> response = restTemplate.exchange("lb://DELIVERY-SERVICE/box/" + boxId + "/deliveries",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
