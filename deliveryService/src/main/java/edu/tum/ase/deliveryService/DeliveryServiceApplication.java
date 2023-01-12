package edu.tum.ase.deliveryService;

import com.mongodb.client.MongoClient;
import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

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

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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
                garching.setId("63bd7e0e6a7619035ecf9b36");
                boxRepository.save(garching);

                Delivery delivery1 = new Delivery("63bd7c881d73037a057a2d91", "63bd7c881d73037a057a2d92", garching);
                Delivery delivery2 = new Delivery("63bd7c881d73037a057a2d91", "63bd7c881d73037a057a2d92", garching);

                garching.addDelivery(delivery1);
                garching.addDelivery(delivery2);
                delivery2.setStatus(DeliveryStatus.IN_TARGET_BOX);
                deliveryRepository.save(delivery1);
                deliveryRepository.save(delivery2);


                boxRepository.save(garching);

                Box schwabing = new Box("Schwabing", "Ludwigstraße 28, 80539", "6789");
                schwabing.setId("63bd7e0e6a7619035ecf9b39");
                boxRepository.save(schwabing);
                Delivery delivery3 = new Delivery("63bd7c881d73037a057a2d91", "63bd7c881d73037a057a2d92", schwabing);
                delivery3.setStatus(DeliveryStatus.DELIVERED);
                deliveryRepository.save(delivery3);
            }
        }
    }

}
