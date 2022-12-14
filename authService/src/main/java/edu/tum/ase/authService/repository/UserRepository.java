package edu.tum.ase.authService.repository;

import edu.tum.ase.authService.model.AseUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<AseUser, String> {
    Optional<AseUser> findByEmail(String email);
}
