package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.backendCommon.roles.UserRole;
import edu.tum.ase.backendCommon.rules.ValidationUtil;
import edu.tum.ase.deliveryService.Util;
import edu.tum.ase.deliveryService.exceptions.UnauthorizedException;
import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.deliveryService.exceptions.UserIsNoValidCustomer;
import edu.tum.ase.deliveryService.exceptions.UserIsNoValidDeliverer;
import edu.tum.ase.deliveryService.request.DeliveryRequest;
import edu.tum.ase.deliveryService.service.BoxService;
import edu.tum.ase.deliveryService.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

import static edu.tum.ase.backendCommon.rules.ValidationUtil.*;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    BoxService boxService;

    @Autowired
    RestTemplate restTemplate;


    public Boolean UserIsValid(String api , String username, String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, cookie);
        HttpEntity<String> entity = new HttpEntity(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(api + username, HttpMethod.GET, entity, Object.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return true;
            } else {
                return false;
            }
        } catch (HttpClientErrorException e) {
            throw new UserIsNoValidCustomer();
        }
    }

    //##################################################################################################################
    // GET mappings

    @GetMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DELIVERER')")
    public List<Delivery> getDeliveries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) authentication.getPrincipal());

        if (Util.isDeliverer(authentication)) {
            return deliveryService.getDeliveriesForDeliverer(userDetails.getUsername());
        }

        if (Util.isCustomer(authentication)) {
            return deliveryService.getDeliveriesForCustomer(userDetails.getUsername());
        }

        throw new UnauthorizedException();
    }

    @GetMapping("all")
    @PreAuthorize("hasRole('DISPATCHER')")
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("{deliveryId}")
    @PreAuthorize("hasAnyRole('DISPATCHER', 'DELIVERER', 'CUSTOMER')")
    public Delivery getDelivery(@PathVariable String deliveryId) {
        Delivery delivery = deliveryService.findById(deliveryId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) authentication.getPrincipal());

        if ((Util.isCustomer(authentication) && !delivery.getCustomer().equals(userDetails.getUsername()))
                || (Util.isDeliverer(authentication) && !delivery.getDeliverer().equals(userDetails.getUsername()))
        ) {
            throw new UnauthorizedException();
        }

        return delivery;
    }

    @GetMapping("{deliveryId}/box")
    @PreAuthorize("hasAnyRole('DISPATCHER', 'DELIVERER', 'CUSTOMER')")
    public Box getDeliveriesForBox(@PathVariable String deliveryId) {
        Delivery delivery = deliveryService.findById(deliveryId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) authentication.getPrincipal());

        if ((Util.isCustomer(authentication) && !delivery.getCustomer().equals(userDetails.getUsername()))
                || (Util.isDeliverer(authentication) && !delivery.getDeliverer().equals(userDetails.getUsername()))
        ) {
            throw new UnauthorizedException();
        }

        return delivery.getBox();
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery createAndAddDelivery(@RequestHeader(HttpHeaders.COOKIE) String cookie, @Validated(OnCreation.class)@RequestBody DeliveryRequest deliveryRequest, @PathVariable String boxId) {

        if (!UserIsValid("lb://auth-service/user/is_customer/", deliveryRequest.getCustomer(), cookie)){
            throw new UserIsNoValidCustomer();
        }
        if (!UserIsValid("lb://auth-service/user/is_deliverer/", deliveryRequest.getDeliverer(), cookie)){
            throw new UserIsNoValidDeliverer();
        }

        Delivery delivery = new Delivery();
        deliveryRequest.apply(delivery);
        Box box = boxService.findById(boxId);
        boxService.assignDelivery(box, delivery);
        return delivery; // delivery is persisted in line above
    }

    //##################################################################################################################
    // PUT mappings

    @PutMapping("{deliveryId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery updateDelivery(@RequestHeader(HttpHeaders.COOKIE) String cookie, @Validated(OnUpdate.class) @RequestBody DeliveryRequest deliveryRequest, @PathVariable String deliveryId) {

        if (!UserIsValid("lb://auth-service/user/is_customer/", deliveryRequest.getCustomer(), cookie)){
            throw new UserIsNoValidCustomer();
        }
        if (!UserIsValid("lb://auth-service/user/is_deliverer/", deliveryRequest.getDeliverer(), cookie)){
            throw new UserIsNoValidDeliverer();
        }

        Delivery delivery = deliveryService.findById(deliveryId);
        deliveryRequest.apply(delivery);
        return deliveryService.updateDelivery(delivery);
    }

    @PutMapping("{deliveryId}/assign/{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery assignDelivery(@PathVariable String deliveryId, @PathVariable String boxId) {
        Delivery delivery = deliveryService.findById(deliveryId);
        Box box = boxService.findById(boxId);
        boxService.assignDelivery(box, delivery);
        return delivery; // delivery is persisted in line above
    }

    @PutMapping("{deliveryId}/pickup")
    @PreAuthorize("hasRole('DELIVERER')")
    public Delivery pickUpDelivery(@PathVariable String deliveryId) {
        Delivery delivery = deliveryService.findById(deliveryId);
        delivery.setStatus(DeliveryStatus.PICKED_UP);

        // TODO: send mail

        deliveryService.updateDelivery(delivery);
        return delivery;
    }

    @PutMapping("place")
    @PreAuthorize("hasRole('RASPI')")
    public HttpStatus placeDeliveries() {
        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Box box = boxService.findById(boxId);
        box = boxService.placeDeliveries(box);
        return HttpStatus.OK;
    }

    @PutMapping("retrieve")
    @PreAuthorize("hasRole('RASPI')")
    public HttpStatus retrieveDeliveries() {
        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Box box = boxService.findById(boxId);
        box = boxService.retrieveDeliveries(box);
        return HttpStatus.OK;
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteDelivery(@PathVariable String id) {
        Delivery delivery = deliveryService.findById(id);
        deliveryService.deleteDelivery(delivery);
        return HttpStatus.OK;
    }
}
