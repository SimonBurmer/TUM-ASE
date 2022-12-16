package edu.tum.ase.authService.service;

import edu.tum.ase.backendCommon.roles.UserRole;
import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AseUser> maybeUser = userRepository.findByEmail(email);

        if (maybeUser.isEmpty()) {
            throw new UsernameNotFoundException(email);
        } else {
            AseUser aseUser = maybeUser.get();
            // DONE: return a Spring AseUser with the
            // email, password and authority that we retrieved above
            return new User(aseUser.getEmail(),
                    aseUser.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    List.of(new SimpleGrantedAuthority(UserRole.getCompleteRole(aseUser.getRoles())))
            );
        }
    }
}
