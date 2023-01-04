package edu.tum.ase.authService.controller;

import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.authService.request.UserRequest;
import edu.tum.ase.authService.service.UserService;
import edu.tum.ase.backendCommon.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    //##################################################################################################################
    // GET mappings

    @GetMapping("")
    @PreAuthorize("hasRole('DISPATCHER')")
    public List<AseUser> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("current")
    public AseUser getCurrentUsers() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userService.findByEmail(email);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("/create")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus createUser(@Valid @RequestBody UserRequest userRequest) {
        AseUser user = new AseUser();

        userRequest.setJwtUtil(jwtUtil);
        userRequest.setBCryptPasswordEncoder(bCryptPasswordEncoder);

        userRequest.apply(user);
        userService.createUser(user);
        return HttpStatus.OK;
    }

    //##################################################################################################################
    // PUT mappings



    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{email}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteUser(@PathVariable String email) {
        AseUser user = userService.findByEmail(email);
        userService.deleteUser(user);
        return HttpStatus.OK;
    }
}
