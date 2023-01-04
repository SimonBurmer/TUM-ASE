package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.Util;
import edu.tum.ase.deliveryService.exceptions.DeliveryStatusException;
import edu.tum.ase.deliveryService.exceptions.SingleCustomerPerBoxViolationException;
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

        throw new UnsupportedOperationException(); // TODO
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

        if (!(box.getDeliveries().isEmpty() || box.getDeliveries().get(0).getCustomer().equals(delivery.getCustomer()))) {
            throw new SingleCustomerPerBoxViolationException();
        }

        box.addDelivery(delivery);
        boxService.updateBox(box);

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

        if (!delivery.getStatus().canBeReassigned()) {
            throw new DeliveryStatusException();
        }

        if (!(box.getDeliveries().isEmpty() || box.getDeliveries().get(0).getCustomer().equals(delivery.getCustomer()))) {
            throw new SingleCustomerPerBoxViolationException();
        }

        boxService.clearDeliveryAssignment(delivery);
        box.addDelivery(delivery);

        boxService.updateBox(box);
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

    @PutMapping("{boxId}/status/{status}")
    @PreAuthorize("hasAnyRole('BOX', 'DELIVERER')")
    public List<Delivery> updateDeliveryStatus(@PathVariable String boxId, @PathVariable DeliveryStatus status) {
        if (!status.canBeAssignedByBox()) {
            throw new UnauthorizedException();
        }

        Box box = boxService.findById(boxId);
        List<Delivery> deliveries = box.getDeliveries();

        for (Delivery delivery : deliveries) {
            delivery.setStatus(status);
        }

        boxService.updateBox(box);

        if (status == DeliveryStatus.DELIVERED) {
            box.getDeliveries().clear();
            boxService.updateBox(box);
        }
        return deliveries;
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteDelivery(@PathVariable String id) {
        Delivery delivery = deliveryService.findById(id);

        if (!delivery.getStatus().canBeRemoved()) {
            throw new DeliveryStatusException();
        }

        Box box = delivery.getBox();
        box.removeDelivery(delivery);
        boxService.updateBox(box);

        deliveryService.deleteDelivery(delivery);
        return HttpStatus.OK;
    }
}
