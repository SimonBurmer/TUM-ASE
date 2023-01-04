package edu.tum.ase.deliveryService.request;

import edu.tum.ase.deliveryService.model.Box;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BoxRequest implements Request<Box> {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String rasPiId;

    @Override
    public void apply(Box box) {
        box.setName(this.name);
        box.setAddress(this.address);
        box.setRasPiId(this.rasPiId);
    }
}
