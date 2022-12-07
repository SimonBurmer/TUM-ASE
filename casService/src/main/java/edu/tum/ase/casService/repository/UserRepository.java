package edu.tum.ase.casService.repository;

import edu.tum.ase.casService.model.AseUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<AseUser, String> {
    Optional<AseUser> findByUsername(String username);
}
