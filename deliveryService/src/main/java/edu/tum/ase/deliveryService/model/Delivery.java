package edu.tum.ase.deliveryService.model;

import org.springframework.data.annotation.Id;

import static edu.tum.ase.deliveryService.model.DeliveryStatus.*;
import static java.util.UUID.randomUUID;

public class Delivery {

    //##################################################################################################################
    // Fields

    @Id
    private String _id;

    private DeliveryStatus status;

    private String customer;

    private String deliverer;

    //##################################################################################################################
    // Constructors

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

    //##################################################################################################################
    // Getters and Setters


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }
}
