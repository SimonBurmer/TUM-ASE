package edu.tum.ase.project.service;

import edu.tum.ase.project.exceptions.BoxAlreadyExistsException;
import edu.tum.ase.project.exceptions.BoxNotFoundException;
import edu.tum.ase.project.model.Box;
import edu.tum.ase.project.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoxService {

    @Autowired
    private BoxRepository boxRepository;

    //##################################################################################################################
    // Create

    public Box createBox(Box box) {
        if (boxRepository.findByLocation(box.getLocation()).isPresent()) {
            throw new BoxAlreadyExistsException();
        }
        return boxRepository.save(box);
    }

    //##################################################################################################################
    // Retrieve

    public Box findByLocation(String location) {
        return boxRepository.findByLocation(location).orElseThrow(BoxNotFoundException::new);
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    //##################################################################################################################
    // Update

    // TODO: do we need this at all?

    //##################################################################################################################
    // Delete

    public void delete(Box box) {
        boxRepository.delete(box);
    }
}
