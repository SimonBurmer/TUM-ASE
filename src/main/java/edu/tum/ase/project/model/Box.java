package edu.tum.ase.project.model;

import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;

@Document(collection = "boxes")
public class Box {

    //##################################################################################################################
    // Fields

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String name;

    @NonNull
    private String address;

    @NonNull
    private String rasPiId;

    // TODO: implement these classes
    //private Optional<Customer> currentCustomer;
    //private Collection<Delivery> deliveries;

    //##################################################################################################################
    // Constructors

    public Box() {
        name = "";
        address = "";
        rasPiId = "";
    }

    public Box(String name, String address, String rasPiId)
    {
        this.name = name;
        this.address = address;
        this.rasPiId = rasPiId;
    }

    //##################################################################################################################
    // Getters and Setters

    public ObjectId getId() {
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
}
