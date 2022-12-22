package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.exceptions.BoxHasActiveDeliveriesException;
import edu.tum.ase.deliveryService.exceptions.BoxHasDeliveredDeliveriesException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/box")
public class BoxController {

    @Autowired
    BoxService boxService;

    //##################################################################################################################
    // GET mappings

    @GetMapping("")
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @GetMapping("{id}")
    public Box getBox(@PathVariable String id) {
        return boxService.findById(id);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("create")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box createBox(@RequestBody Box box) {
        return boxService.createBox(box);
    }

    //##################################################################################################################
    // PUT mappings
    @PutMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box updateBox(@RequestBody Box newBox, @PathVariable String id) {
        Box box = boxService.findById(id);

        // Check for not yet delivered "deliveries"
        Collection<Delivery> deliveries = box.getDeliveries();
        for (Delivery delivery : deliveries) {
            if (!delivery.getStatus().equals(DeliveryStatus.DELIVERED)){
                throw new BoxHasDeliveredDeliveriesException();
            }
        }

        newBox.setId(id);
        return boxService.updateBox(newBox);
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteBox(@PathVariable String id) {
        Box box = boxService.findById(id);

        // Check for active deliveries
        Collection<Delivery> deliveries = box.getDeliveries();
        if (deliveries.size() > 0) {
            throw new BoxHasActiveDeliveriesException();
        } else {
            boxService.delete(box);
        }
        return HttpStatus.OK;
    }
}
