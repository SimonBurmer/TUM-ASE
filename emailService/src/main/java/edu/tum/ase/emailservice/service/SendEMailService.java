package edu.tum.ase.emailservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendEMailService {

    @Autowired
    private JavaMailSender emailSender;

    private String notificationCreatedSubject = "New delivery";
    private String notificationCreatedText = "Your delivery %s is on its way.";


    private String notificationInTargetBoxSubject = "Your delivery is ready to be picked up";
    private String notificationInTargetBoxText = "Your delivery %s is ready to be picked up at %s.";


    private String notificationDeliveredSubject = "Delivered";
    private String notificationDeliveredText = "Your delivery %s has been delivered.";
    private String notificationMultipleDeliveredText = "Your deliveries %s have been delivered.";

    public void sendNotificationCreated(String to, String deliveryId) {
        String text = String.format(notificationCreatedText, deliveryId);
        this.sendEMail(to, notificationCreatedSubject, text);
    }

    public void sendNotificationInTargetBox(String to, String deliveryId) {
        String text = String.format(notificationInTargetBoxText, deliveryId);
        this.sendEMail(to, notificationInTargetBoxSubject, text);
    }

    public void sendNotificationDelivered(String to, List<String> deliveryIds) {
        String deliveryIdsText = deliveryIds.stream()
                .collect(Collectors.joining(", "));
        String text = deliveryIds.size() > 1 ? String.format(notificationMultipleDeliveredText, deliveryIdsText) :
                String.format(notificationDeliveredText, deliveryIdsText);
        this.sendEMail(to, notificationDeliveredSubject, text);
    }

    private void sendEMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
