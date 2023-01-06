package edu.tum.ase.authService.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateBearerRequest {

    @NotEmpty(message = "id is required")
    private final String id;

    public CreateBearerRequest() {
        this.id = "";
    }

    public CreateBearerRequest(String id) {
        this.id = id;
    }

}
