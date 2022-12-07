package edu.tum.ase.project;

import com.mongodb.client.MongoClient;
import edu.tum.ase.project.repository.BoxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableEurekaClient
public class DeliveryServiceApplication implements CommandLineRunner {

    @Autowired
    MongoClient mongoClient;

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }

}
