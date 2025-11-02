package com.service.ev_service.backend.services.NoitificationServices.repository;

import com.service.ev_service.backend.services.NoitificationServices.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}