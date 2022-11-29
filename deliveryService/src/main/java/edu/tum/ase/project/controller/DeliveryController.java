package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.Box;
import edu.tum.ase.project.model.Delivery;
import edu.tum.ase.project.model.DeliveryStatus;
import edu.tum.ase.project.service.BoxService;
import edu.tum.ase.project.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/box/{boxId}/delivery")
public class DeliveryController {

    @Autowired
    BoxService boxService;

    @Autowired
    DeliveryService deliveryService;

    //##################################################################################################################
    // GET mappings

    @GetMapping("")
    public Collection<Delivery> getDeliveriesForBox(@PathVariable String boxId) {
        Box box = boxService.findById(boxId);
        return deliveryService.findDeliveriesForBox(box);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("")
    public Delivery createDeliveryForBox(@PathVariable String boxId, @RequestBody Delivery delivery) {
        Box box = boxService.findById(boxId);
        delivery = deliveryService.createDelivery(box, delivery);
        return delivery;
    }

    //##################################################################################################################
    // PUT mappings

    @PutMapping("{deliveryId}/status")
    public HttpStatus updateDeliveryStatusForBox(@PathVariable String boxId, @PathVariable String deliveryId, @RequestBody DeliveryStatus deliveryStatus) {
        Box box = boxService.findById(boxId);
        Delivery delivery = deliveryService.findDelivery(box, deliveryId);
        deliveryService.updateStatus(box, delivery, deliveryStatus);

        return HttpStatus.OK;
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{deliveryId}")
    public HttpStatus deleteDeliveryForBox(@PathVariable String boxId, @PathVariable String deliveryId) {
        Box box = boxService.findById(boxId);
        Delivery delivery = deliveryService.findDelivery(box, deliveryId);
        deliveryService.delete(box, delivery);

        return HttpStatus.OK;
    }
}
