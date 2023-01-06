package edu.tum.ase.deliveryService.repository;

import edu.tum.ase.backendCommon.model.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {

    Optional<Delivery> findById(String id);

    @Query("{'customer': ?0}")
    List<Delivery> findDeliveriesForCustomer(String customer);

    @Query("{'deliverer': ?0}")
    List<Delivery> findDeliveriesForDeliverer(String deliverer);
}
