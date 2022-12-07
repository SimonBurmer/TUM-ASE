package edu.tum.ase.casService.service;

import edu.tum.ase.casService.model.AseUser;
import edu.tum.ase.casService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DONE: Call the repository to find the user from a given username
        Optional<AseUser> maybeUser = userRepository.findByUsername(username);

        if (maybeUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        } else {
            AseUser user = maybeUser.get();
            // DONE: return a Spring User with the
            // username, password and authority that we retrieved above
            return new User(user.getUsername(),
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    List.of(new SimpleGrantedAuthority(user.getRole()))
            );
        }
    }
}
