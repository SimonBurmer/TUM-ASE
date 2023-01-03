package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.exceptions.BoxHasPendingDeliveriesException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.request.BoxRequest;
import edu.tum.ase.deliveryService.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("{boxId}")
    public Box getBox(@PathVariable String boxId) {
        return boxService.findById(boxId);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("create")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box createBox(@Valid @RequestBody BoxRequest boxRequest) {
        Box box = new Box();
        boxRequest.apply(box);
        return boxService.createBox(box);
    }


    //##################################################################################################################
    // PUT mappings

    @PutMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box updateBox(@Valid @RequestBody BoxRequest boxRequest, @PathVariable String boxId) {
        Box box = boxService.findById(boxId);

        // Check for not yet delivered "deliveries"
        if (box.hasPendingDeliveries()) {
            throw new BoxHasPendingDeliveriesException();
        }

        boxRequest.apply(box);
        return boxService.updateBox(box);
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteBox(@PathVariable String boxId) {
        Box box = boxService.findById(boxId);

        // Check for not yet delivered "deliveries"
        if (box.hasPendingDeliveries()) {
            throw new BoxHasPendingDeliveriesException();
        }

        boxService.deleteBox(box);
        return HttpStatus.OK;
    }
}
