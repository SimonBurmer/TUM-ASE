package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.exceptions.DeliveryStatusException;
import edu.tum.ase.deliveryService.exceptions.SingleCustomerPerBoxViolationException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.request.DeliveryRequest;
import edu.tum.ase.deliveryService.service.BoxService;
import edu.tum.ase.deliveryService.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('DISPATCHER')")
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("{deliveryId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery getDelivery(@PathVariable String deliveryId) {
        return deliveryService.findById(deliveryId);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery createAndAddDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest, @PathVariable String boxId) {
        Delivery delivery = new Delivery();
        deliveryRequest.apply(delivery);

        Box box = boxService.findById(boxId);
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

        if (delivery.getStatus() == DeliveryStatus.DELIVERED)
            boxService.clearDeliveryAssignment(delivery);

        return deliveryService.updateDelivery(delivery); // delivery is persisted in line above
    }

    @PutMapping("{deliveryId}/assign/{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery assignDelivery(@PathVariable String deliveryId, @PathVariable String boxId) {
        Delivery delivery = deliveryService.findById(deliveryId);
        Box box = boxService.findById(boxId);

        if (!delivery.getStatus().canBeReassigned()) {
            throw new DeliveryStatusException();
        }

        if (!box.getDeliveries().get(0).getCustomer().equals(delivery.getCustomer())) {
            throw new SingleCustomerPerBoxViolationException();
        }

        boxService.clearDeliveryAssignment(delivery);
        box.addDelivery(delivery);

        boxService.updateBox(box);
        return delivery; // delivery is persisted in line above
    }


    // I don't think this is necessary
    /*@PutMapping("{boxId}/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box removeDeliveryFromBox(@PathVariable String boxId, @PathVariable String id) {
        Box box = boxService.findById(boxId);
        Delivery delivery = deliveryService.findById(id);

        Collection<Delivery> deliveries = box.getDeliveries();
        for (Delivery iterDelivery : deliveries) {
            if (iterDelivery.getId().equals(id)){
                box.removeDelivery(delivery);
                //deliveryService.deleteDelivery(delivery);

                return boxService.updateBox(box);
            }
        }
        throw new DeliveryNotPartOfBoxException();
    }*/


    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteDelivery(@PathVariable String id) {
        Delivery delivery = deliveryService.findById(id);

        if (!delivery.getStatus().canBeRemoved()) {
            throw new DeliveryStatusException();
        }

        deliveryService.deleteDelivery(delivery);
        return HttpStatus.OK;
    }
}
