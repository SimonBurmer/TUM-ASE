package edu.tum.ase.authService.request;

import edu.tum.ase.authService.rules.BoxValidationRule;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateBearerRequest {

    @NotEmpty(message = "id is required")
    @BoxValidationRule
    private final String id;

    public CreateBearerRequest() {
        this.id = "";
    }

    public CreateBearerRequest(String id) {
        this.id = id;
    }

}
