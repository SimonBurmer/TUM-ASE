package edu.tum.ase.authService.controller;

import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.authService.request.UserRequest;
import edu.tum.ase.authService.service.UserService;
import edu.tum.ase.backendCommon.jwt.JwtUtil;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static edu.tum.ase.backendCommon.rules.ValidationUtil.*;

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
    @PreAuthorize("hasAnyRole('DISPATCHER', 'DELIVERER', 'CUSTOMER')")
    public AseUser getCurrentUsers() {
        String id = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userService.findById(id);
    }

    //##################################################################################################################
    // POST mappings

    @PostMapping("/create")
    @PreAuthorize("hasRole('DISPATCHER')")
    public AseUser createUser(@Valid @Validated(OnCreation.class) @RequestBody UserRequest userRequest) {
        AseUser user = new AseUser();

        userRequest.setJwtUtil(jwtUtil);
        userRequest.setBCryptPasswordEncoder(bCryptPasswordEncoder);

        userRequest.apply(user);
        return userService.createUser(user);
    }

    //##################################################################################################################
    // PUT mappings

    @PutMapping("{userId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public AseUser updateUser(@Valid @Validated(OnUpdate.class) @RequestBody UserRequest userRequest, @PathVariable String userId) {
        AseUser user = userService.findById(userId);

        userRequest.setJwtUtil(jwtUtil);
        userRequest.setBCryptPasswordEncoder(bCryptPasswordEncoder);

        userRequest.apply(user);
        return userService.updateUser(user);
    }

    //##################################################################################################################
    // DELETE mappings

    @DeleteMapping("{userId}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public HttpStatus deleteUser(@PathVariable String userId, HttpServletRequest currentRequest) {
        AseUser user = userService.findById(userId);
        userService.deleteUser(currentRequest, user);
        return HttpStatus.OK;
    }

    //##################################################################################################################
    // User Checks
    @PreAuthorize("hasRole('DISPATCHER')")
    @GetMapping("is_customer/{userId}")
    public ResponseEntity<Boolean> userIsCustomer(@PathVariable String userId){
        if (userService.findById(userId).getRole().equals("CUSTOMER")) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasRole('DISPATCHER')")
    @GetMapping("is_deliverer/{userId}")
    public ResponseEntity<Boolean> userIsDeliverer(@PathVariable String userId){
        if (userService.findById(userId).getRole().equals("DELIVERER")) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
