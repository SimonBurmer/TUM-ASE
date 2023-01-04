package edu.tum.ase.deliveryService;

import com.mongodb.client.MongoClient;
import edu.tum.ase.backendCommon.roles.UserRole;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import edu.tum.ase.deliveryService.request.DeliveryRequest;
import edu.tum.ase.deliveryService.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = {"edu.tum.ase.backendCommon", "edu.tum.ase.deliveryService"})
@EnableEurekaClient
public class DeliveryServiceApplication implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    BoxRepository boxRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("MongoClient = " + mongoClient.getClusterDescription());

        if (Arrays.stream(environment.getActiveProfiles()).noneMatch(p -> p.equals("test"))) {
            // Only create dummy boxes and deliveries when there is no other box in DB
            if (boxRepository.findAll().size() == 0) {
                Box garching = new Box("Garching", "Bolzmannstr. 3, 85748 Garching", "1234");
                boxRepository.save(garching);

                Delivery delivery1 = new Delivery("bob@priv.de", "alice@apd.de", garching);
                Delivery delivery2 = new Delivery("bob@priv.de", "alice@apd.de", garching);

                garching.addDelivery(delivery1);
                garching.addDelivery(delivery2);
                delivery2.setStatus(DeliveryStatus.IN_TARGET_BOX);
                deliveryRepository.save(delivery1);
                deliveryRepository.save(delivery2);


                boxRepository.save(garching);

                Box schwabing = new Box("Schwabing", "Ludwigstraße 28, 80539", "6789");
                boxRepository.save(schwabing);
                Delivery delivery3 = new Delivery("bob@priv.de", "alice@apd.de", schwabing);
                delivery3.setStatus(DeliveryStatus.DELIVERED);
                deliveryRepository.save(delivery3);
            }
        }
    }

}
