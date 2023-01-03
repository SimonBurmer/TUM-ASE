package edu.tum.ase.deliveryService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.lang.NonNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static edu.tum.ase.deliveryService.model.DeliveryStatus.ORDERED;


@Data
@Document()
@JsonIgnoreProperties("box")
public class Delivery {

    //##################################################################################################################
    // Fields

    @Id
    private String id;

    @NonNull

    private DeliveryStatus status;

    @NonNull

    private String customer;

    @NonNull
    private String deliverer;
    public Delivery() {
        status = ORDERED;
        customer = "";
        deliverer = "";
    }

    public Delivery(String customer, String deliverer) {
        status = ORDERED;
        this.customer = customer;
        this.deliverer = deliverer;
    }
}
