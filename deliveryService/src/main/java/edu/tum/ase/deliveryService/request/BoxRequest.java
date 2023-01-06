package edu.tum.ase.deliveryService.request;

import edu.tum.ase.backendCommon.request.Request;
import edu.tum.ase.backendCommon.model.Box;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BoxRequest implements Request<Box> {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "rasPiId is required")
    private String rasPiId;

    @Override
    public void apply(Box box) {
        box.setName(this.name);
        box.setAddress(this.address);
        box.setRasPiId(this.rasPiId);
    }
}
