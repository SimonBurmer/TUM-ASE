package edu.tum.ase.project.model;

import com.mongodb.lang.NonNull;
import edu.tum.ase.project.exceptions.SingleCustomerPerBoxViolationException;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Document(collection = "boxes")
public class Box {

    //##################################################################################################################
    // Fields

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String name;

    @NonNull
    private String address;

    @NonNull
    private String rasPiId;

    private Collection<Delivery> deliveries;

    //##################################################################################################################
    // Constructors

    public Box() {
        name = "";
        address = "";
        rasPiId = "";
        this.deliveries = new HashSet<>();
    }

    public Box(String name, String address, String rasPiId)
    {
        this.name = name;
        this.address = address;
        this.rasPiId = rasPiId;
        this.deliveries = new HashSet<>();
    }

    //##################################################################################################################
    // Getters and Setters

    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getRasPiId() {
        return rasPiId;
    }

    public void setRasPiId(@NonNull String rasPiId) {
        this.rasPiId = rasPiId;
    }

    public Collection<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Collection<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }

    public void removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }
}
