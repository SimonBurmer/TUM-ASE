package edu.tum.ase.backendCommon.model;

import com.fasterxml.jackson.annotation.*;
import com.mongodb.lang.NonNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import static edu.tum.ase.backendCommon.model.DeliveryStatus.ORDERED;


@Data
@Document()
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

    @NonNull
    @DBRef()
    @JsonIgnore
    private Box box;

    public Delivery() {
        status = ORDERED;
        customer = "";
        deliverer = "";
    }

    public Delivery(String customer, String deliverer, Box box) {
        status = ORDERED;
        this.customer = customer;
        this.deliverer = deliverer;
        this.box = box;
    }
}
