package edu.tum.ase.backendCommon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class AseUser {

    @Id
    @NonNull
    protected String id;

    @NonNull
    @Indexed(unique = true)
    private String email;

    @NonNull
    @JsonIgnore
    private String password;

    @NonNull
    private String role;

    @Nullable
    private String rfid;

    public AseUser() {
        email = "";
        password = "";
        role = "";
        rfid = "";
    }

    public AseUser(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        rfid = null;
    }

    public AseUser(String email, String password, String role, String rfid) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.rfid = rfid;
    }
}
