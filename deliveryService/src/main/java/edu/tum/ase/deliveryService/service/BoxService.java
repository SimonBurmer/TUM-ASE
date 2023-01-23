package edu.tum.ase.deliveryService.service;

import edu.tum.ase.deliveryService.DeliveryServiceApplication;
import edu.tum.ase.deliveryService.exceptions.*;
import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BoxService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceApplication.class);

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
        deliveryRepository.saveAll(deliveries);

        log.info("Creating box " + box);
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

        // Check for not yet delivered "deliveries"
        if (box.hasPendingDeliveries()) {
            throw new BoxHasPendingDeliveriesException();
        }

        Collection<Delivery> deliveries = box.getDeliveries();
        deliveryRepository.saveAll(deliveries);
        log.info("Updating box " + box);
        return boxRepository.save(box);
    }

    public Box assignDelivery(Box box, Delivery delivery) {
        if (!delivery.getStatus().canBeModified()) {
            throw new DeliveryModificationNotAllowedException();
        }

        box.addDelivery(delivery);

        // TODO: send email (Role DISPATCHER)
        log.info("Assigning delivery " + delivery + " to box "+ box);
        deliveryRepository.save(delivery);
        return boxRepository.save(box);
    }

    public Box placeDeliveries(Box box, String delivererId) {
        List<Delivery> deliveries = box.getDeliveries();

        for (Delivery delivery : deliveries) {
            if (delivery.getDeliverer().equals(delivererId) && delivery.getStatus().equals(DeliveryStatus.PICKED_UP)) {
                delivery.setStatus(DeliveryStatus.IN_TARGET_BOX);
                deliveryRepository.save(delivery);

                // TODO: send email (Role RASPI)
            }
        }

        return boxRepository.save(box);
    }


    public Box retrieveDeliveries(Box box) {
        List<Delivery> deliveries = box.getDeliveries();

        List<Delivery> shouldBeRemoved = new ArrayList<>();

        for (Delivery delivery : deliveries) {
            if (delivery.getStatus().equals(DeliveryStatus.IN_TARGET_BOX)) {
                delivery.setStatus(DeliveryStatus.DELIVERED);

                // TODO: send email (Role RASPI)

                shouldBeRemoved.add(delivery);
                deliveryRepository.save(delivery);
            }
        }

        for (Delivery delivery : shouldBeRemoved) {
            box.removeDelivery(delivery);
        }

        return boxRepository.save(box);
    }

    //##################################################################################################################
    // Delete
    public void deleteBox(Box box) {
        // Check for not yet delivered "deliveries"
        if (box.hasPendingDeliveries()) {
            throw new BoxHasPendingDeliveriesException();
        }

        log.info("Deleting box " + box);
        boxRepository.delete(box);
    }
}
