package edu.tum.ase.backendCommon.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;
import edu.tum.ase.deliveryService.exceptions.SingleCustomerPerBoxViolationException;
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
    @JsonIgnore
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
        if (!(getDeliveries().isEmpty() || getDeliveries().get(0).getCustomer().equals(delivery.getCustomer()))) {
            throw new SingleCustomerPerBoxViolationException();
        }

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

    @Override
    public String toString() {
        return "Box{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rasPiId='" + rasPiId + '\'' +
                '}';
    }
}
