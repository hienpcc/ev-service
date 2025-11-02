package com.service.ev_service.backend.services.NoitificationServices.scheduler;

// Cập nhật import để trỏ đến các class trong package của bạn
import com.service.ev_service.backend.services.NoitificationServices.dto.CustomerServicePackageDTO;
import com.service.ev_service.backend.services.NoitificationServices.dto.LastServiceRecordDTO;
import com.service.ev_service.backend.services.NoitificationServices.dto.VehicleDTO;
import com.service.ev_service.backend.services.NoitificationServices.services.NotificationCreationService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ReminderScheduler {

    private final RestTemplate restTemplate;
    private final NotificationCreationService notificationCreationService;

    @Value("${services.customer.url}")
    private String customerServiceUrl;

    @Value("${services.maintenance.url}")
    private String maintenanceServiceUrl;

    @Value("${services.billing.url}")
    private String billingServiceUrl;


    public ReminderScheduler(NotificationCreationService notificationCreationService) {
        this.restTemplate = new RestTemplate();
        this.notificationCreationService = notificationCreationService;
    }

    @Scheduled(cron = "0 0 8 * * ?") // Chạy vào 8h sáng mỗi ngày
    public void checkAndSendReminders() {
        System.out.println("Running scheduled reminder check at " + LocalDate.now());
        checkMaintenanceReminders();
        checkServicePackageReminders();
    }

    private void checkMaintenanceReminders() {
        try {
            VehicleDTO[] vehicles = restTemplate.getForObject(customerServiceUrl + "/vehicles", VehicleDTO[].class);
            if (vehicles == null) return;

            for (VehicleDTO vehicle : vehicles) {
                LastServiceRecordDTO lastService = restTemplate.getForObject(
                        maintenanceServiceUrl + "/vehicles/" + vehicle.vehicleId() + "/last-service",
                        LastServiceRecordDTO.class
                );

                LocalDate nextMaintenanceDate;
                if (lastService != null) {
                    nextMaintenanceDate = lastService.date().plusMonths(6); // Logic: 6 tháng 1 lần
                } else {
                    nextMaintenanceDate = LocalDate.of(vehicle.year(), 1, 1).plusYears(1); // Logic: 1 năm sau khi sản xuất
                }

                long daysUntilMaintenance = ChronoUnit.DAYS.between(LocalDate.now(), nextMaintenanceDate);
                if (daysUntilMaintenance <= 7 && daysUntilMaintenance > 0) {
                    String message = String.format(
                            "Xe %s của bạn sắp đến hạn bảo dưỡng định kỳ vào ngày %s.",
                            vehicle.model(),
                            nextMaintenanceDate
                    );
                    notificationCreationService.createNotification(
                            vehicle.customerId(),
                            "MAINTENANCE_REMINDER",
                            message
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error during maintenance reminder check: " + e.getMessage());
        }
    }

    private void checkServicePackageReminders() {
        try {
            CustomerServicePackageDTO[] packages = restTemplate.getForObject(billingServiceUrl + "/packages/expiring", CustomerServicePackageDTO[].class);
            if (packages == null) return;

            for (CustomerServicePackageDTO pkg : packages) {
                String message = String.format(
                        "Gói dịch vụ %s của bạn sẽ hết hạn vào ngày %s. Vui lòng gia hạn.",
                        pkg.packageName(),
                        pkg.endDate()
                );
                notificationCreationService.createNotification(
                        pkg.customerId(),
                        "PACKAGE_EXPIRATION_REMINDER",
                        message
                );
            }
        } catch (Exception e) {
            System.err.println("Error during service package reminder check: " + e.getMessage());
        }
    }
}