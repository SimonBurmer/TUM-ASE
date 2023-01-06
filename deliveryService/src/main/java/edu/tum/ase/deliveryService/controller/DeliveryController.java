package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.Util;
import edu.tum.ase.deliveryService.exceptions.UnauthorizedException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.request.DeliveryRequest;
import edu.tum.ase.deliveryService.service.BoxService;
import edu.tum.ase.deliveryService.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    BoxService boxService;


    //##################################################################################################################
    // GET mappings

    @GetMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DELIVERER')")
    public List<Delivery> getDeliveries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) authentication.getPrincipal());

        if (authentication.getAuthorities().stream().anyMatch((a) -> a.getAuthority().equals("ROLE_DELIVERER"))) {
            return deliveryService.getDeliveriesForDeliverer(userDetails.getUsername());
        }

        if (authentication.getAuthorities().stream().anyMatch((a) -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
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

    //##################################################################################################################
    // POST mappings

    @PostMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery createAndAddDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest, @PathVariable String boxId) {
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
    public Delivery updateDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest, @PathVariable String deliveryId) {
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
        deliveryService.updateDelivery(delivery);
        return delivery;
    }

    @PutMapping("{boxId}/place")
    @PreAuthorize("hasAnyRole('RASPI')")
    public List<Delivery> placeDeliveries() {
        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Box box = boxService.findById(boxId);
        box = boxService.placeDeliveries(box);
        return box.getDeliveries();
    }

    @PutMapping("{boxId}/retrieve")
    @PreAuthorize("hasAnyRole('RASPI')")
    public List<Delivery> retrieveDeliveries() {
        String boxId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Box box = boxService.findById(boxId);
        box = boxService.retrieveDeliveries(box);
        return box.getDeliveries();
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
