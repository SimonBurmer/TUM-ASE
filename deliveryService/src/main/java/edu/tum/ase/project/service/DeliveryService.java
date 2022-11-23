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
import java.util.Optional;

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

    public Collection<Delivery> findDeliveriesForBox(Box box) {
        return box.getDeliveries();
    }

    //##################################################################################################################
    // Update
    public void updateStatus(Box box, Delivery delivery) {
        Optional<Delivery> maybeDelivery = box.getDeliveries().stream().filter(d -> d.equals(delivery)).findFirst();
        if (maybeDelivery.isPresent()) {
            maybeDelivery.get().setStatus(delivery.getStatus());
            boxRepository.save(box);
        } else {
            throw new DeliveryNotFoundException();
        }
    }

    //##################################################################################################################
    // Delete

    public void deleteFromBox(Box box, Delivery delivery) {
        for (Delivery boxDelivery : box.getDeliveries()) {
            // TODO: this requires improvement
            if (boxDelivery.equals(delivery)) {
                delivery = boxDelivery;
                break;
            }
        }
        box.removeDelivery(delivery);
        boxRepository.save(box);
    }

    public void delete(Delivery delivery) {
        for (Box box : boxRepository.findAll()) {
            // TODO: this requires database design improvements
            if (box.getDeliveries().contains(delivery)) {
                box.removeDelivery(delivery);
                boxRepository.save(box);
            }
        }
    }


}
