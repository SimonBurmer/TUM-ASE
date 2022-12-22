package edu.tum.ase.deliveryService.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static edu.tum.ase.deliveryService.model.DeliveryStatus.*;
import static java.util.UUID.randomUUID;


@Data
@Document(collection = "deliveries")
public class Delivery {

    //##################################################################################################################
    // Fields

    @Id
    private String _id;

    private DeliveryStatus status;

    private String customer;

    private String deliverer;


    public Delivery() {
        _id = randomUUID() + "";
        status = ORDERED;
        customer = "";
        deliverer = "";
    }

    public Delivery(String customer, String deliverer) {
        _id = randomUUID() + "";
        status = ORDERED;
        this.customer = customer;
        this.deliverer = deliverer;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
