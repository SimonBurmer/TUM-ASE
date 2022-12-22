package edu.tum.ase.deliveryService.repository;

import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {

    Optional<Delivery> findById(String id);
}
