package edu.tum.ase.emailservice.controller;

import edu.tum.ase.backendCommon.request.EMailMultipleNotificationsRequest;
import edu.tum.ase.backendCommon.request.EMailNotificationRequest;
import edu.tum.ase.emailservice.service.EMailAddressService;
import edu.tum.ase.emailservice.service.SendEMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/email")
public class EMailController {

    @Autowired
    private SendEMailService sendEMailService;

    @Autowired
    private EMailAddressService eMailAddressService;

    @PostMapping("notificationCreated")
    public void sendCreatedNotification(@RequestBody EMailNotificationRequest requestBody, HttpServletRequest request) {
        String recipient = this.eMailAddressService.getEMailAddressByUserId(HttpHeaders.COOKIE,
                request.getHeader(HttpHeaders.COOKIE), requestBody.getCustomerId());
        this.sendEMailService.sendNotificationCreated(recipient, requestBody.getDeliveryId());
    }

    @PostMapping("notificationInTargetBox")
    public void sendPickupNotification(@RequestBody EMailNotificationRequest requestBody, HttpServletRequest request) {
        String recipient = this.eMailAddressService.getEMailAddressByUserId(HttpHeaders.AUTHORIZATION,
                request.getHeader(HttpHeaders.AUTHORIZATION), requestBody.getCustomerId());
        this.sendEMailService.sendNotificationInTargetBox(recipient, requestBody.getDeliveryId());
    }

    @PostMapping("notificationDelivered")
    public void sendDeliveredNotification(@RequestBody EMailMultipleNotificationsRequest requestBody, HttpServletRequest request) {
        String recipient = this.eMailAddressService.getEMailAddressByUserId(HttpHeaders.AUTHORIZATION,
                request.getHeader(HttpHeaders.AUTHORIZATION), requestBody.getCustomerId());
        this.sendEMailService.sendNotificationDelivered(recipient, requestBody.getDeliveryIds());
    }
}
