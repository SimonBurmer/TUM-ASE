package edu.tum.ase.authService.request;

import com.mongodb.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class AuthRequest {


    @Email
    @NonNull
    private final String email;

    @NonNull
    @NotEmpty
    private final String password_enc;

    public AuthRequest(String email, String password_enc) {
        this.email = email;
        this.password_enc = password_enc;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword_enc() {
        return password_enc;
    }
}
