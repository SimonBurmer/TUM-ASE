package edu.tum.ase.project.model;

import static edu.tum.ase.project.model.DeliveryStatus.*;

public class Delivery {

    //##################################################################################################################
    // Fields

    private DeliveryStatus status;

    private String customer;

    private String deliverer;

    //##################################################################################################################
    // Constructors

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

    //##################################################################################################################
    // Getters and Setters

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Delivery delivery = (Delivery) o;

        // do not consider status
        if (!customer.equals(delivery.customer)) return false;
        return deliverer.equals(delivery.deliverer);
    }

    @Override
    public int hashCode() {
        // do not consider status
        int result = customer.hashCode();
        result = 31 * result + deliverer.hashCode();
        return result;
    }
}
