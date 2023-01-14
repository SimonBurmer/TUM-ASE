package edu.tum.ase.deliveryService.service;

import edu.tum.ase.deliveryService.DeliveryServiceApplication;
import edu.tum.ase.deliveryService.exceptions.DeliveryNotFoundException;
import edu.tum.ase.deliveryService.exceptions.DeliveryStatusException;
import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceApplication.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private BoxRepository boxRepository;

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
        if (!delivery.getStatus().canBeModified()) {
            throw new DeliveryStatusException();
        }

        log.info("Updating delivery " + delivery);
        return deliveryRepository.save(delivery);
    }

    //##################################################################################################################
    // Delete

    public void deleteDelivery(Delivery delivery) {
        if (!delivery.getStatus().canBeRemoved()) {
            throw new DeliveryStatusException();
        }

        Box box = delivery.getBox();
        box.removeDelivery(delivery);
        boxRepository.save(box);

        log.info("Deleting delivery " + delivery);
        deliveryRepository.delete(delivery);
    }
}
