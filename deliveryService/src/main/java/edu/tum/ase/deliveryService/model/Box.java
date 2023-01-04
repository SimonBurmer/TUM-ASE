package edu.tum.ase.deliveryService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mongodb.lang.NonNull;
import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Document()
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

    @Indexed(unique = true)
    @NonNull
    private String rasPiId;

    @DBRef() //db = "deliveries"
    @JsonBackReference
    private List<Delivery> deliveries;

    public Box() {
        this.name = "";
        this.address = "";
        this.rasPiId = "";
        this.deliveries = new LinkedList<>();
    }

    public Box(String name, String address, String rasPiId)
    {
        this.name = name;
        this.address = address;
        this.rasPiId = rasPiId;
        this.deliveries = new LinkedList<>();
    }


    public void addDelivery(Delivery delivery) {
        if (delivery.getBox() != null) {
            delivery.getBox().removeDelivery(delivery);
        }

        delivery.setBox(this);
        this.deliveries.add(delivery);
    }

    public void removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }

    public boolean hasPendingDeliveries() {
        return !this.deliveries.isEmpty();
    }
}
