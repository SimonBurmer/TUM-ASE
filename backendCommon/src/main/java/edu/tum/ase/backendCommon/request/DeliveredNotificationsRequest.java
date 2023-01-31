package edu.tum.ase.backendCommon.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class DeliveredNotificationsRequest {

    @NotEmpty(message = "customerId is required")
    private final String customerId;

    @NotEmpty(message = "deliveryIds are required")
    private final List<String> deliveryIds;

}
