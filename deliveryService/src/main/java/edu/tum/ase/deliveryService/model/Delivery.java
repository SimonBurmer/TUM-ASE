package edu.tum.ase.deliveryService.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static edu.tum.ase.deliveryService.model.DeliveryStatus.*;
import static java.util.UUID.randomUUID;


@Data
@Document()
public class Delivery {

    //##################################################################################################################
    // Fields

    @Id
    private String id;

    private DeliveryStatus status;

    private String customer;

    private String deliverer;


    public Delivery() {
        id = randomUUID() + "";
        status = ORDERED;
        customer = "";
        deliverer = "";
    }

    public Delivery(String customer, String deliverer) {
        id = randomUUID() + "";
        status = ORDERED;
        this.customer = customer;
        this.deliverer = deliverer;
    }
}
