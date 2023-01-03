package edu.tum.ase.deliveryService.request;

import edu.tum.ase.deliveryService.model.Box;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class BoxRequest implements Request<Box> {

    private String name;

    private String address;

    private String rasPiId;

    @Override
    public void apply(Box box) {
        if (this.name != null)
            box.setName(this.name);
        if (this.address != null)
            box.setAddress(this.address);
        if (this.rasPiId != null)
            box.setRasPiId(this.rasPiId);
    }
}
