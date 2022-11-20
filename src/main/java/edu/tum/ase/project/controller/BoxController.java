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

    @GetMapping("{name}")
    public Box getBoxByName(@PathVariable String name) {
        return boxService.findByName(name);
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

    @DeleteMapping("{name}")
    public HttpStatus deleteBoxByName(@PathVariable String name) {
        Box box = boxService.findByName(name);
        boxService.delete(box);

        return HttpStatus.OK;
    }

}
