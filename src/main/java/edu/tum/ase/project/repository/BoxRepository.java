package edu.tum.ase.project.repository;

import edu.tum.ase.project.model.Box;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BoxRepository extends MongoRepository<Box, ObjectId> {

    Optional<Box> findByName(String name);
    Optional<Box> findByAddress(String location);
    Optional<Box> findByRasPiId(String rasPiId);
}
