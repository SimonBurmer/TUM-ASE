package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.Box;
import edu.tum.ase.project.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Box createBox(@RequestBody Box box) {
        return boxService.createBox(box);
    }

    //##################################################################################################################
    // PUT mappings

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{id}")
    public HttpStatus deleteBox(@PathVariable String id) {
        Box box = boxService.findById(id);
        boxService.delete(box);

        return HttpStatus.OK;
    }

}
