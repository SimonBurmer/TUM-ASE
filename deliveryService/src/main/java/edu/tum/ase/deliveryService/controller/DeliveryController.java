package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.exceptions.BoxHasActiveDeliveriesException;
import edu.tum.ase.deliveryService.exceptions.BoxHasDeliveredDeliveriesException;
import edu.tum.ase.deliveryService.exceptions.DeliveryNotPartOfBoxException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.service.BoxService;
import edu.tum.ase.deliveryService.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    BoxService boxService;

    @GetMapping("")
    @PreAuthorize("hasRole('DISPATCHER')")
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery getDelivery(@PathVariable String id) {
        return deliveryService.findById(id);
    }

    @PostMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box createAndAddDelivery(@RequestBody Delivery delivery, @PathVariable String boxId) {
        Box box = boxService.findById(boxId);
        box.addDelivery(delivery);

        return boxService.updateBox(box);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Delivery updateDelivery(@RequestBody Delivery newdelivery, @PathVariable String id) {

        newdelivery.setId(id);
        return deliveryService.updateDelivery(newdelivery);
    }

    @DeleteMapping("{boxId}/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteDeliveryFromBox(@PathVariable String boxId, @PathVariable String id) {
        Box box = boxService.findById(boxId);
        Delivery delivery = deliveryService.findById(id);

        Collection<Delivery> deliveries = box.getDeliveries();
        for (Delivery iterDelivery : deliveries) {
            if (iterDelivery.getId().equals(id)){
                deliveryService.deleteDelivery(delivery);
                return HttpStatus.OK;
            }
        }
        throw new DeliveryNotPartOfBoxException();
    }


    // TODO: Maybe GET BOX of delivery?
    // TODO: Maybe delete delivery?
}
