package edu.tum.ase.deliveryService.service;

import edu.tum.ase.deliveryService.exceptions.BoxAlreadyExistsException;
import edu.tum.ase.deliveryService.exceptions.BoxNotFoundException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
public class BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    //##################################################################################################################
    // Create

    public Box createBox(Box box) {
        if (boxRepository.findByName(box.getName()).isPresent()) {
            throw new BoxAlreadyExistsException();
        }

        Collection<Delivery> deliveries = box.getDeliveries();
        for (Delivery delivery : deliveries) {
            deliveryRepository.save(delivery);
        }

        return boxRepository.save(box);
    }

    //##################################################################################################################
    // Retrieve

    public Box findById(String id) {
        return boxRepository.findById(id).orElseThrow(BoxNotFoundException::new);
    }

    public Box findByName(String name) {
        return boxRepository.findByName(name).orElseThrow(BoxNotFoundException::new);
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    //##################################################################################################################
    // Update

    public Box updateBox(Box box) {

    //            oldBox.setAddress(newBox.getAddress());
    //            oldBox.setName(newBox.getName());
    //            oldBox.setRasPiId(newBox.getRasPiId());
    //            oldBox.setDeliveries(newBox.getDeliveries());

        return boxRepository.save(box);
    }

    //##################################################################################################################
    // Delete
    public void deleteBox(Box box) {
        boxRepository.delete(box);
    }
}
