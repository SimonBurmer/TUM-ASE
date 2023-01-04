package edu.tum.ase.deliveryService.request;

import edu.tum.ase.backendCommon.request.Request;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.rules.CustomerValidationRule;
import edu.tum.ase.deliveryService.rules.DelivererValidationRule;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeliveryRequest implements Request<Delivery> {

    @NotBlank(message = "customer is require")
    @CustomerValidationRule(message = "customer needs to be a valid customer")
    private String customer;

    @NotBlank(message = "deliverer is required")
    @DelivererValidationRule(message = "deliverer needs to be a valid deliverer")
    private String deliverer;

    @Override
    public void apply(Delivery delivery) {
        delivery.setCustomer(this.customer);
        delivery.setDeliverer(this.deliverer);
    }
}
