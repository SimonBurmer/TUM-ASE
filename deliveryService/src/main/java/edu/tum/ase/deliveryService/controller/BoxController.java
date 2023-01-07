package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.rules.ValidationUtil;
import edu.tum.ase.deliveryService.Util;
import edu.tum.ase.deliveryService.exceptions.UnauthorizedException;
import edu.tum.ase.deliveryService.request.BoxRequest;
import edu.tum.ase.deliveryService.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static edu.tum.ase.backendCommon.rules.ValidationUtil.*;

@RestController
@RequestMapping("/box")
public class BoxController {

    @Autowired
    BoxService boxService;

    //##################################################################################################################
    // GET mappings

    @GetMapping("")
    @PreAuthorize("hasRole('DISPATCHER')")
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @GetMapping("{boxId}")
    @PreAuthorize("hasAnyRole('DISPATCHER', 'DELIVERER', 'CUSTOMER')")
    public Box getBox(@PathVariable String boxId) {
        return boxService.findById(boxId);
    }

    @GetMapping("{boxId}/deliveries")
    @PreAuthorize("hasAnyRole('DISPATCHER', 'RASPI')")
    public List<Delivery> getDeliveriesForBox(@PathVariable String boxId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Util.isRasPi(authentication) && !((UserDetails) authentication.getPrincipal()).getUsername().equals(boxId)) {
            throw new UnauthorizedException();
        }

        return boxService.findById(boxId).getDeliveries();
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("create")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box createBox(@Valid @Validated(OnCreation.class) @RequestBody BoxRequest boxRequest) {
        Box box = new Box();
        boxRequest.apply(box);
        return boxService.createBox(box);
    }


    //##################################################################################################################
    // PUT mappings

    @PutMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public Box updateBox(@Valid @Validated(OnUpdate.class) @RequestBody BoxRequest boxRequest, @PathVariable String boxId) {
        Box box = boxService.findById(boxId);
        boxRequest.apply(box);
        return boxService.updateBox(box);
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{boxId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteBox(@PathVariable String boxId) {
        Box box = boxService.findById(boxId);
        boxService.deleteBox(box);
        return HttpStatus.OK;
    }
}
