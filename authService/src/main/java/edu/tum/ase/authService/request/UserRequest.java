package edu.tum.ase.authService.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.tum.ase.backendCommon.model.AseUser;
import edu.tum.ase.backendCommon.jwt.JwtUtil;
import edu.tum.ase.backendCommon.request.Request;
import edu.tum.ase.backendCommon.roles.UserRole;
import edu.tum.ase.backendCommon.rules.ValidationUtil;
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

    @NotBlank(message = "email is required", groups = {ValidationUtil.OnCreation.class})
    @Email
    private String email;

    @NotBlank(message = "password_enc is required and should be encrypted as JWE", groups = {ValidationUtil.OnCreation.class})
    private String password_enc;

    @NotBlank(message = "role is required", groups = {ValidationUtil.OnCreation.class})
    @Pattern(regexp = UserRole.DELIVERER + "|" + UserRole.CUSTOMER + "|" + UserRole.DISPATCHER, message = "Role should be either DISPATCHER, DELIVERER or CUSTOMER")
    private String role;

    private String rfid;

    @Override
    public void apply(AseUser other) {
        if (other.getRole() == null || other.getRole().equals("")) {
            if (role == null)
                throw new ValidationException("role is required");
            else {
                other.setRole(role);
                if ((role.equals(UserRole.CUSTOMER) || role.equals(UserRole.DELIVERER)) && rfid == null)
                    throw new ValidationException("Such a user requires a rfid token");
            }
        }

        if (password_enc != null && !password_enc.equals("")) {
            if (jwtUtil != null) {
                String password = jwtUtil.decryptJwe(password_enc);
                other.setPassword(bCryptPasswordEncoder.encode(password));
            }
        }

        if (email != null && !email.equals("")) {
            other.setEmail(email);
        }

        if (rfid != null && !rfid.equals("")) {
            if ((other.getRole().equals(UserRole.CUSTOMER) || other.getRole().equals(UserRole.DELIVERER)))
                other.setRfid(rfid);
        }
    }
}
