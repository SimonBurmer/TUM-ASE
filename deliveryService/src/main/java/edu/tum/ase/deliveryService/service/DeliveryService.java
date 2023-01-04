package edu.tum.ase.deliveryService.service;

import edu.tum.ase.deliveryService.exceptions.BoxAlreadyExistsException;
import edu.tum.ase.deliveryService.exceptions.DeliveryNotFoundException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    //##################################################################################################################
    // Create

    //##################################################################################################################
    // Retrieve

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery findById(String id) {
        return deliveryRepository.findById(id).orElseThrow(DeliveryNotFoundException::new);
    }

    public List<Delivery> getDeliveriesForDeliverer(String deliverer) {
        return deliveryRepository.findDeliveriesForDeliverer(deliverer);
    }

    public List<Delivery> getDeliveriesForCustomer(String customer) {
        return deliveryRepository.findDeliveriesForCustomer(customer);
    }

    //##################################################################################################################
    // Update

    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    //##################################################################################################################
    // Delete

    public void deleteDelivery(Delivery delivery) {
        deliveryRepository.delete(delivery);
    }
}
