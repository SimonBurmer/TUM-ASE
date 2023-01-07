package edu.tum.ase.authService.service;

import edu.tum.ase.authService.exceptions.UserAlreadyExistsException;
import edu.tum.ase.authService.exceptions.UserNotFoundException;
import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //##################################################################################################################
    // Create

    public AseUser createUser(AseUser user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        return userRepository.save(user);
    }

    //##################################################################################################################
    // Retrieve

    public AseUser findById(String id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public AseUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public AseUser findByRfid(String rfid) {
        return userRepository.findByRfid(rfid).orElseThrow(UserNotFoundException::new);
    }

    public List<AseUser> getAllUsers() {
        return userRepository.findAll();
    }

    //##################################################################################################################
    // Update

    public AseUser updateUser(AseUser user) {
        return userRepository.save(user);
    }

    //##################################################################################################################
    // Delete
    public void deleteUser(AseUser user) {
        // TODO: also delete associated deliveries?
        userRepository.delete(user);
    }
}
