package edu.tum.ase.casService.service;

import edu.tum.ase.backendCommon.roles.UserRole;
import edu.tum.ase.casService.model.AseUser;
import edu.tum.ase.casService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AseUser> maybeUser = userRepository.findByEmail(username);

        if (maybeUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        } else {
            AseUser aseUser = maybeUser.get();
            // DONE: return a Spring AseUser with the
            // username, password and authority that we retrieved above
            return new User(aseUser.getEmail(),
                    aseUser.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    aseUser.getRoles().stream()
                            .map(UserRole::getCompleteRole)
                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
        }
    }
}
