package edu.tum.ase.authService;

import com.mongodb.client.MongoClient;
import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.authService.repository.UserRepository;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = {"edu.tum.ase.backendCommon", "edu.tum.ase.authService"})
public class AuthServiceApplication implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("MongoClient = " + mongoClient.getClusterDescription());

        if (Arrays.stream(environment.getActiveProfiles()).noneMatch(p -> p.equals("test"))) {
            // Only create dummy users when there is no other user in DB
            if (userRepository.findAll().size() == 0) {
                AseUser bob = new AseUser("bob@priv.de", bCryptPasswordEncoder.encode("password"), UserRole.CUSTOMER, "customer001");
                AseUser alice = new AseUser("alice@apd.de", bCryptPasswordEncoder.encode("test1234"), UserRole.DELIVERER, "deliverer001");
                AseUser kate = new AseUser("kate@apd.de", bCryptPasswordEncoder.encode("xyz"), UserRole.DISPATCHER);

                userRepository.save(bob);
                userRepository.save(alice);
                userRepository.save(kate);
            }
        }
    }
}
