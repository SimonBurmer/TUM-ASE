package edu.tum.ase.deliveryService.model;

import com.mongodb.lang.NonNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.HashSet;

@Document(collection = "boxes")
@Data
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

    @DBRef(db = "deliveries")
    private Collection<Delivery> deliveries;


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


    public void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }

    public void removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }
}
