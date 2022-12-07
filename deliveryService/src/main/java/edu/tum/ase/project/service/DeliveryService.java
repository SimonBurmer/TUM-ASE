package edu.tum.ase.project.service;

import edu.tum.ase.project.exceptions.DeliveryNotFoundException;
import edu.tum.ase.project.exceptions.SingleCustomerPerBoxViolationException;
import edu.tum.ase.project.model.Box;
import edu.tum.ase.project.model.Delivery;
import edu.tum.ase.project.model.DeliveryStatus;
import edu.tum.ase.project.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DeliveryService {

    @Autowired
    private BoxRepository boxRepository;

    //##################################################################################################################
    // Create

    public Delivery createDelivery(Box box, Delivery delivery) {
        if (box.getDeliveries().stream().anyMatch(b -> !b.getCustomer().equals(delivery.getCustomer()))) {
            throw new SingleCustomerPerBoxViolationException();
        }
        box.addDelivery(delivery);
        boxRepository.save(box);
        return delivery;
    }

    //##################################################################################################################
    // Retrieve

    public Delivery findDelivery(Box box, String id) {
        return box.getDeliveries().stream()
                .filter((d)->d.get_id().equals(id))
                .findFirst()
                .orElseThrow(DeliveryNotFoundException::new);
    }

    public Collection<Delivery> findDeliveriesForBox(Box box) {
        return box.getDeliveries();
    }

    //##################################################################################################################
    // Update
    public void updateStatus(Box box, Delivery delivery, DeliveryStatus deliveryStatus) {
        delivery.setStatus(deliveryStatus);
        boxRepository.save(box);
    }

    //##################################################################################################################
    // Delete

    public void delete(Box box, Delivery delivery) {
        box.removeDelivery(delivery);
        boxRepository.save(box);
    }
}
