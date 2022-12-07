package edu.tum.ase.project.repository;

import edu.tum.ase.project.model.Box;
import edu.tum.ase.project.model.Delivery;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BoxRepository extends MongoRepository<Box, ObjectId> {

    Optional<Box> findById(String id);
    Optional<Box> findByName(String name);
    Optional<Box> findByAddress(String location);
    Optional<Box> findByRasPiId(String rasPiId);

    @Query("{'deliveries': ?0}")
    Optional<Box> findBoxForDelivery(Delivery delivery);
}
