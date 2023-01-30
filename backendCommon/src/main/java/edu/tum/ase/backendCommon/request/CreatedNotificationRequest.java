package edu.tum.ase.backendCommon.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreatedNotificationRequest {

    @NotEmpty(message = "customerId is required")
    private final String customerId;

    @NotEmpty(message = "deliveryId is required")
    private final String deliveryId;
}
