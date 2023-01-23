package edu.tum.ase.emailservice.controller;

import edu.tum.ase.emailservice.service.EMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EMailController {

    @Autowired
    private EMailService eMailService;

    @PostMapping("")
    public void sendEmail(){
        this.eMailService.sendEMail("", "", "");
    }
}
