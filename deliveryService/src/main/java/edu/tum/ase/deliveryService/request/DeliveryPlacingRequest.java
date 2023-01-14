package edu.tum.ase.deliveryService.request;

import edu.tum.ase.backendCommon.rules.ValidationUtil;
import edu.tum.ase.deliveryService.rules.DelivererValidationRule;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeliveryPlacingRequest {
    @NotBlank(message = "deliverer is required", groups = {ValidationUtil.OnCreation.class})
    @DelivererValidationRule(message = "deliverer needs to be a valid deliverer")
    private String deliverer;
}
