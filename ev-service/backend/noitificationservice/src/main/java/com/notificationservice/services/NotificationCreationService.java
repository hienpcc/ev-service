package com.notificationservice.services;

import com.notificationservice.model.Notification;
import com.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationCreationService {

    private final NotificationRepository notificationRepository;

    public NotificationCreationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(Long customerId, String type, String message) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setType(type);
        notification.setMessage(message);

        notificationRepository.save(notification);
        System.out.println("Created notification for customer " + customerId + ": " + message);
    }
}