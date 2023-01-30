package edu.tum.ase.authService.service;

import edu.tum.ase.authService.exceptions.UserAlreadyExistsException;
import edu.tum.ase.authService.exceptions.UserHasDeliveriesException;
import edu.tum.ase.authService.exceptions.UserNotFoundException;
import edu.tum.ase.authService.exceptions.UserSelfDeletionException;
import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.authService.repository.UserRepository;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    //##################################################################################################################
    // Create

    public AseUser createUser(AseUser user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        return userRepository.save(user);
    }

    //##################################################################################################################
    // Retrieve

    public AseUser findById(String id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public AseUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public AseUser findByRfid(String rfid) {
        return userRepository.findByRfid(rfid).orElseThrow(UserNotFoundException::new);
    }

    public List<AseUser> getAllUsers() {
        return userRepository.findAll();
    }

    //##################################################################################################################
    // Update

    public AseUser updateUser(AseUser user) {
        return userRepository.save(user);
    }

    //##################################################################################################################
    // Delete
    public void deleteUser(HttpServletRequest currentRequest,  AseUser user) {
        String currentUserId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (user.getId().equals(currentUserId)) {
            throw new UserSelfDeletionException();
        }

        if (hasDeliveries(currentRequest, user)) {
            throw new UserHasDeliveriesException();
        }

        userRepository.delete(user);
    }

    private boolean hasDeliveries(HttpServletRequest httpRequest, AseUser user) {
        HttpHeaders headers = new HttpHeaders();
        String header = httpRequest.getHeader(HttpHeaders.COOKIE);
        headers.set(HttpHeaders.COOKIE, header);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Predicate<Delivery> predicate;
        if (user.getRole().equals(UserRole.DELIVERER)) {
            predicate = (d) -> d.getDeliverer().equals(user.getId());
        } else if (user.getRole().equals(UserRole.CUSTOMER)) {
            predicate = (d) -> d.getCustomer().equals(user.getId());
        } else {
            return false;
        }

        ResponseEntity<List<Delivery>> response = restTemplate.exchange("lb://DELIVERY-SERVICE/delivery/all", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        return response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().stream().anyMatch(predicate);
    }
}
