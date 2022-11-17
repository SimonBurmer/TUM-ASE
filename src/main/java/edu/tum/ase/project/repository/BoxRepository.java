package edu.tum.ase.project.repository;

import edu.tum.ase.project.model.Box;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BoxRepository extends MongoRepository<Box, String> {

    Optional<Box> findByLocation(String location);
}
