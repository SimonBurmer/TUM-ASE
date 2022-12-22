package edu.tum.ase.deliveryService.service;

import edu.tum.ase.deliveryService.exceptions.BoxNotFoundException;
import edu.tum.ase.deliveryService.exceptions.DeliveryNotFoundException;
import edu.tum.ase.deliveryService.exceptions.SingleCustomerPerBoxViolationException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery findById(String id) {
        return deliveryRepository.findById(id).orElseThrow(BoxNotFoundException::new);
    }
}
