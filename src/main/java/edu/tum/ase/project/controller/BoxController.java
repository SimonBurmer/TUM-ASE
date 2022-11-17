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
        // DONE: Implement
        return boxService.getAllBoxes();
    }

    @GetMapping("{location}")
    public Box getBoxByLocation(@PathVariable String location) {
        return boxService.findByLocation(location);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("create")
    public Box createBox(@RequestBody String location) {
        return boxService.createBox(new Box(location));
    }

    //##################################################################################################################
    // PUT mappings

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{location}")
    public HttpStatus deleteBoxByLocation(@PathVariable String location) {
        Box box = boxService.findByLocation(location);
        boxService.delete(box);

        return HttpStatus.OK;
    }

}
