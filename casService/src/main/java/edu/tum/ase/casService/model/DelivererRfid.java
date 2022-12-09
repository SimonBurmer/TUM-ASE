package edu.tum.ase.casService.model;

import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "delivererTokens")
public class DelivererRfid {

    String rfidToken;

    String userId;

    public DelivererRfid(String rfidToken, String userId) {
        this.rfidToken = rfidToken;
        this.userId = userId;
    }

    public String getRfidToken() {
        return rfidToken;
    }

    public void setRfidToken(String rfidToken) {
        this.rfidToken = rfidToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
