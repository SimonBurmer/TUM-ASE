package edu.tum.ase.deliveryService.controller;

import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteBox(@PathVariable String id) {
        Box box = boxService.findById(id);
        boxService.delete(box);

        return HttpStatus.OK;
    }

}
