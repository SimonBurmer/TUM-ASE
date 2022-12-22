package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.service.BoxService;
import edu.tum.ase.deliveryService.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {


    @Autowired
    DeliveryService deliveryService;

    //##################################################################################################################
    // GET mappings

    @GetMapping("")
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("{id}")
    public Delivery getDelivery(@PathVariable String id) {
        return deliveryService.findById(id);
    }

    // Update delivery

    // Delete delivery from box


    // TODO: GET BOX of delivery?

}
