package edu.tum.ase.deliveryService.request;

import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.model.Delivery;
import edu.tum.ase.deliveryService.model.DeliveryStatus;
import edu.tum.ase.deliveryService.rules.CustomerValidationRule;
import edu.tum.ase.deliveryService.rules.DelivererValidationRule;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeliveryRequest implements Request<Delivery>{

    private DeliveryStatus status;

    @CustomerValidationRule
    private String customer;

    @DelivererValidationRule
    private String deliverer;

    @Override
    public void apply(Delivery delivery) {
        if (this.status != null)
            delivery.setStatus(this.status);
        if (this.customer != null)
            delivery.setCustomer(this.customer);
        if (this.deliverer != null)
            delivery.setDeliverer(this.deliverer);
    }
}
