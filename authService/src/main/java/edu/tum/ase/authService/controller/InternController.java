package edu.tum.ase.authService.controller;

import edu.tum.ase.authService.service.UserService;
import edu.tum.ase.backendCommon.model.AseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/intern")
public class InternController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    // No authorization here, this endpoint is only used internally between microservices, not mapped by gateway
    public AseUser getUserById(@PathVariable String userId) {
        AseUser user = userService.findById(userId);
        return user;
    }
}
