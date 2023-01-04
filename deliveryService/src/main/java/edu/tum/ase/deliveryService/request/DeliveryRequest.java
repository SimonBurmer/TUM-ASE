package edu.tum.ase.deliveryService.request;

import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.rules.CustomerValidationRule;
import edu.tum.ase.deliveryService.rules.DelivererValidationRule;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeliveryRequest implements Request<Delivery> {

    @NotBlank
    @CustomerValidationRule
    private String customer;

    @NotBlank
    @DelivererValidationRule
    private String deliverer;

    @Override
    public void apply(Delivery delivery) {
        delivery.setCustomer(this.customer);
        delivery.setDeliverer(this.deliverer);
    }
}
