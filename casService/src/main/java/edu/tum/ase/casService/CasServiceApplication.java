package edu.tum.ase.casService;

import com.mongodb.client.MongoClient;
import edu.tum.ase.backendCommon.roles.UserRole;
import edu.tum.ase.casService.model.AseUser;
import edu.tum.ase.casService.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication(scanBasePackages = {"edu.tum.ase.backendCommon", "edu.tum.ase.casService"})
public class CasServiceApplication implements CommandLineRunner {

    @Autowired
    MongoClient mongoClient;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger log = LoggerFactory.getLogger(CasServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CasServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("MongoClient = " + mongoClient.getClusterDescription());

        // Only create dummy users when there is no other user in DB
        if (userRepository.findAll().size() == 0) {
            AseUser bob = new AseUser("Bob", bCryptPasswordEncoder.encode("password"), List.of(UserRole.CUSTOMER));
            AseUser alice = new AseUser("Alice", bCryptPasswordEncoder.encode("test1234"), List.of(UserRole.DELIVERER));
            AseUser kate = new AseUser("Kate", bCryptPasswordEncoder.encode("xyz"), List.of(UserRole.DISPATCHER));

            userRepository.save(bob);
            userRepository.save(alice);
            userRepository.save(kate);
        }
    }

}
