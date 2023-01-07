package edu.tum.ase.authService.request;

import com.mongodb.lang.NonNull;
import edu.tum.ase.backendCommon.rules.ValidationUtil;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class AuthRequest {


    @NotEmpty(message = "email is required")
    @Email(message = "email should be a valid email address")
    private final String email;

    @NotEmpty(message = "password_enc is required")
    private final String password_enc;

    private boolean remember = false;

    public AuthRequest(String email, String password_enc, boolean remember) {
        this.email = email;
        this.password_enc = password_enc;
        this.remember = remember;
    }
}
