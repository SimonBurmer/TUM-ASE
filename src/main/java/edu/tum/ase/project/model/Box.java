package edu.tum.ase.project.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boxes")
public class Box {

    //##################################################################################################################
    // Fields

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String location;

    //##################################################################################################################
    // Constructors

    public Box(String location) {
        this.location = location;
    }

    //##################################################################################################################
    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
