package edu.tum.ase.casService;

import com.mongodb.client.MongoClient;
import edu.tum.ase.casService.model.AseUser;
import edu.tum.ase.casService.model.UserRole;
import edu.tum.ase.casService.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    public void run(String... args) throws Exception {
        log.info("MongoClient = " + mongoClient.getClusterDescription());

        // Only create test users when there is no user in DB
        if (userRepository.findAll().size() == 0) {
        // TODO: Create test users with hashed Bcrypt password and role
            AseUser bob = new AseUser("Bob", bCryptPasswordEncoder.encode("password"), UserRole.MANAGER);
            AseUser alice = new AseUser("Alice", bCryptPasswordEncoder.encode("topsecret"), UserRole.DEV);

            userRepository.save(bob);
            userRepository.save(alice);
        }
    }

}
