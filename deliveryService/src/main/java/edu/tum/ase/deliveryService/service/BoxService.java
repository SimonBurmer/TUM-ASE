package edu.tum.ase.deliveryService.service;

import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.backendCommon.request.CreatedNotificationRequest;
import edu.tum.ase.backendCommon.request.DeliveredNotificationsRequest;
import edu.tum.ase.backendCommon.request.InTargetBoxNotificationRequest;
import edu.tum.ase.deliveryService.DeliveryServiceApplication;
import edu.tum.ase.deliveryService.exceptions.BoxAlreadyExistsException;
import edu.tum.ase.deliveryService.exceptions.BoxHasPendingDeliveriesException;
import edu.tum.ase.deliveryService.exceptions.BoxNotFoundException;
import edu.tum.ase.deliveryService.exceptions.DeliveryModificationNotAllowedException;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoxService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceApplication.class);

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RestTemplate restTemplate;

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

        log.info("Assigning delivery " + delivery + " to box " + box);
        deliveryRepository.save(delivery);

        CreatedNotificationRequest request = new CreatedNotificationRequest(delivery.getCustomer(), delivery.getId());
        this.restTemplate.postForEntity("lb://EMAIL-SERVICE/email/notificationCreated", request, String.class);

        return boxRepository.save(box);
    }

    public Box placeDeliveries(Box box, String delivererId) {
        List<Delivery> deliveries = box.getDeliveries();

        for (Delivery delivery : deliveries) {
            if (delivery.getDeliverer().equals(delivererId) && delivery.getStatus().equals(DeliveryStatus.PICKED_UP)) {
                delivery.setStatus(DeliveryStatus.IN_TARGET_BOX);
                deliveryRepository.save(delivery);
                log.info("Delivery " + delivery + " has been placed in " + box + " - status: " + delivery.getStatus());

                InTargetBoxNotificationRequest request = new InTargetBoxNotificationRequest(delivery.getCustomer(),
                        delivery.getId(), box.getAddress());
                this.restTemplate.postForEntity("lb://EMAIL-SERVICE/email/notificationInTargetBox", request, String.class);
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

                shouldBeRemoved.add(delivery);
                deliveryRepository.save(delivery);
            }
        }

        for (Delivery delivery : shouldBeRemoved) {
            box.removeDelivery(delivery);
            log.info("Delivery " + delivery.getId() + " has been picked up by customer "
                    + delivery.getDeliverer() + " at box " + box + " - status: " + delivery.getStatus());
        }

        if (!shouldBeRemoved.isEmpty()) {
            String customerId = shouldBeRemoved.get(0).getCustomer();
            List<String> deliveryIds = shouldBeRemoved.stream().map(Delivery::getId).collect(Collectors.toList());
            DeliveredNotificationsRequest request = new DeliveredNotificationsRequest(customerId, deliveryIds);

            this.restTemplate.postForEntity("lb://EMAIL-SERVICE/email/notificationDelivered", request, String.class);
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
