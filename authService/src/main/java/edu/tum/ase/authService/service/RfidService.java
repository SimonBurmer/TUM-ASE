package edu.tum.ase.authService.service;

import edu.tum.ase.backendCommon.model.AseUser;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RfidService {

    @Autowired
    FetchDeliveriesUtil util;

    public boolean checkCustomerAccess(HttpServletRequest currentRequest, AseUser customer) {
        if (!customer.getRole().equals(UserRole.CUSTOMER))
            return false;

        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<Delivery> deliveries = util.fetchDeliveries(currentRequest, boxId);

        return deliveries.stream()
                .filter(d -> d.getStatus().equals(DeliveryStatus.IN_TARGET_BOX))
                .anyMatch(d -> d.getCustomer().equals(customer.getId()));
    }


    public boolean checkDelivererAccess(HttpServletRequest currentRequest, AseUser deliverer) {
        if (!deliverer.getRole().equals(UserRole.DELIVERER))
            return false;

        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<Delivery> deliveries = util.fetchDeliveries(currentRequest, boxId);

        return deliveries.stream()
                .filter(d -> d.getStatus().equals(DeliveryStatus.PICKED_UP))
                .anyMatch(d -> d.getDeliverer().equals(deliverer.getId()));
    }


    @Component
    public static class FetchDeliveriesUtil {
        // Moved into inner class for mocking purposes

        @Autowired
        private RestTemplate restTemplate;

        public List<Delivery> fetchDeliveries(HttpServletRequest currentRequest, String boxId) {
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
}
