package edu.tum.ase.authService.controller;

import edu.tum.ase.authService.exceptions.BoxAccessException;
import edu.tum.ase.authService.model.AseUser;
import edu.tum.ase.authService.service.RfidService;
import edu.tum.ase.authService.service.UserService;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rfid")
public class RfidController {

    @Autowired
    UserService userService;

    @Autowired
    RfidService rfidService;

    @GetMapping("{rfid}")
    @PreAuthorize("hasRole('RASPI')")
    public AseUser checkRfid(@PathVariable String rfid, HttpServletRequest request) {
        AseUser user = userService.findByRfid(rfid);

        if (user.getRole().equals(UserRole.CUSTOMER) && rfidService.checkCustomerAccess(request, user)) {
            return user;
        }

        if (user.getRole().equals(UserRole.DELIVERER) && rfidService.checkDelivererAccess(request, user)) {
            return user;
        }
        throw new BoxAccessException();
    }
}
