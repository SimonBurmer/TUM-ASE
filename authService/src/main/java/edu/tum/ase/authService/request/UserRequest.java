package edu.tum.ase.authService.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.tum.ase.authService.exceptions.MissingRfidException;
import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.backendCommon.jwt.JwtUtil;
import edu.tum.ase.backendCommon.request.Request;
import edu.tum.ase.backendCommon.roles.UserRole;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.ValidationException;
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

    @Pattern(regexp = UserRole.DELIVERER + "|" + UserRole.CUSTOMER + "|" + UserRole.DISPATCHER, message = "Role should be either DISPATCHER, DELIVERER or CUSTOMER")
    private String role;

    private String rfid;

    @Override
    public void apply(AseUser other) {
        if (other.getRole() == null || other.getRole().equals("")) {
            if (role == null)
                throw new ValidationException("role is required");
            else
                other.setRole(role);
        }

        if ((other.getRole().equals(UserRole.CUSTOMER) || other.getRole().equals(UserRole.DELIVERER)) && rfid == null)
            throw new MissingRfidException();

        if (jwtUtil != null) {
            String password = jwtUtil.decryptJwe(password_enc);
            other.setPassword(bCryptPasswordEncoder.encode(password));
        }

        other.setEmail(email);

        if ((other.getRole().equals(UserRole.CUSTOMER) || other.getRole().equals(UserRole.DELIVERER)))
            other.setRfid(rfid);
    }
}
