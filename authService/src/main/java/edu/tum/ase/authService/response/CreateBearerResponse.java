package edu.tum.ase.authService.response;

import lombok.Data;

@Data
public class CreateBearerResponse {

    private final String id;

    private final String token;

    public CreateBearerResponse(String id, String token) {
        this.id = id;
        this.token = token;
    }
}
