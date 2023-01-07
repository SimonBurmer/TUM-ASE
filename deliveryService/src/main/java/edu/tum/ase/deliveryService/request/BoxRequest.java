package edu.tum.ase.deliveryService.request;

import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.request.Request;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static edu.tum.ase.backendCommon.rules.ValidationUtil.OnCreation;

@Data
public class BoxRequest implements Request<Box> {

    @NotBlank(message = "name is required", groups = {OnCreation.class})
    private String name;

    @NotBlank(message = "address is required", groups = {OnCreation.class})
    private String address;

    @NotBlank(message = "rasPiId is required", groups = {OnCreation.class})
    private String rasPiId;

    @Override
    public void apply(Box box) {
        if (name != null && !name.equals(""))
            box.setName(name);
        if (address != null && !address.equals(""))
            box.setAddress(address);
        if (rasPiId != null && !rasPiId.equals(""))
            box.setRasPiId(rasPiId);
    }
}
