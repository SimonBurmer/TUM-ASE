package edu.tum.ase.authService.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.tum.ase.authService.exceptions.MissingRfidException;
import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.backendCommon.jwt.JwtUtil;
import edu.tum.ase.backendCommon.request.Request;
import edu.tum.ase.backendCommon.roles.UserRole;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserRequest implements Request<AseUser> {

    @JsonIgnore
    private JwtUtil jwtUtil;

    @JsonIgnore
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @NotBlank(message = "email is required")
    @Email
    private String email;

    @NotBlank(message = "password_enc is required and should be encrypted as JWE")
    private String password_enc;

    @NotBlank(message = "role is required")
    @Pattern(regexp = UserRole.DELIVERER + "|" + UserRole.CUSTOMER + "|" + UserRole.DISPATCHER, message = "Role should be either DISPATCHER, DELIVERER or CUSTOMER")
    private String role;

    private String rfid;

    @Override
    public void apply(AseUser other) {
        if ((role.equals(UserRole.CUSTOMER) || role.equals(UserRole.DELIVERER)) && rfid == null)
            throw new MissingRfidException();

        if (jwtUtil != null) {
            String password = jwtUtil.decryptJwe(password_enc);
            other.setPassword(bCryptPasswordEncoder.encode(password));
        }

        other.setEmail(email);
        other.setRole(role);

        if ((role.equals(UserRole.CUSTOMER) || role.equals(UserRole.DELIVERER)))
            other.setRole(rfid);
    }
}
