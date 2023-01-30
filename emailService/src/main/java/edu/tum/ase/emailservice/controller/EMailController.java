package edu.tum.ase.emailservice.controller;

import edu.tum.ase.backendCommon.request.CreatedNotificationRequest;
import edu.tum.ase.backendCommon.request.DeliveredNotificationsRequest;
import edu.tum.ase.backendCommon.request.InTargetBoxNotificationRequest;
import edu.tum.ase.emailservice.service.EMailAddressService;
import edu.tum.ase.emailservice.service.SendEMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EMailController {

    @Autowired
    private SendEMailService sendEMailService;

    @Autowired
    private EMailAddressService eMailAddressService;

    @PostMapping("notificationCreated")
    public void sendCreatedNotification(@RequestBody CreatedNotificationRequest requestBody) {
        String recipient = this.eMailAddressService.getEMailAddressByUserId(requestBody.getCustomerId());
        this.sendEMailService.sendNotificationCreated(recipient, requestBody.getDeliveryId());
    }

    @PostMapping("notificationInTargetBox")
    public void sendPickupNotification(@RequestBody InTargetBoxNotificationRequest requestBody) {
        String recipient = this.eMailAddressService.getEMailAddressByUserId(requestBody.getCustomerId());
        this.sendEMailService.sendNotificationInTargetBox(recipient, requestBody.getDeliveryId(), requestBody.getAddress());
    }

    @PostMapping("notificationDelivered")
    public void sendDeliveredNotification(@RequestBody DeliveredNotificationsRequest requestBody) {
        String recipient = this.eMailAddressService.getEMailAddressByUserId(requestBody.getCustomerId());
        this.sendEMailService.sendNotificationDelivered(recipient, requestBody.getDeliveryIds());
    }
}
