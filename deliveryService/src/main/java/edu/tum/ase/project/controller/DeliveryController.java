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
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    BoxService boxService;

    @Autowired
    DeliveryService deliveryService;

    //##################################################################################################################
    // GET mappings

    @GetMapping("{id}")
    public Collection<Delivery> getDeliveriesForBox(@PathVariable String id) {
        Box box = boxService.findById(id);
        return deliveryService.findDeliveriesForBox(box);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("{id}/create")
    public Delivery createDeliveryForBox(@PathVariable String id, @RequestBody Delivery delivery) {
        Box box = boxService.findById(id);
        delivery = deliveryService.createDelivery(box, delivery);
        return delivery;
    }

    //##################################################################################################################
    // PUT mappings

    @PutMapping("status")
    public HttpStatus updateDeliveryStatusForBox(@RequestBody Delivery delivery) {
        Box box = boxService.findBoxForDelivery(delivery);
        deliveryService.updateStatus(box, delivery);

        return HttpStatus.OK;
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{id}")
    public HttpStatus deleteDeliveryForBox(@PathVariable String id, @RequestBody Delivery delivery) {
        Box box = boxService.findByName(id);
        deliveryService.deleteFromBox(box, delivery);

        return HttpStatus.OK;
    }
}
