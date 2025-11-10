/*
-- FILE NÀY ĐÃ ĐƯỢC SỬA CÁC LỖI SAU:
-- 1. Thêm CREATE DATABASE IF NOT EXISTS customer_service;
-- 2. Thêm dấu chấm phẩy (;) sau TẤT CẢ các lệnh USE.
*/

-- I. TẠO CÁC SCHEMA
CREATE DATABASE IF NOT EXISTS customer_service; -- (LỖI 1 ĐÃ SỬA)
CREATE DATABASE IF NOT EXISTS notification_service;
CREATE DATABASE IF NOT EXISTS maintenance_service;
CREATE DATABASE IF NOT EXISTS appointment_service;
CREATE DATABASE IF NOT EXISTS billing_service;
CREATE DATABASE IF NOT EXISTS account_service;

-- II. TẠO VÀ CẤP QUYỀN CHO 6 USER ỨNG DỤNG RIÊNG BIỆT

-- 1. Customer Service
CREATE USER 'customer_user'@'%' IDENTIFIED BY 'customer_password';
GRANT ALL PRIVILEGES ON customer_service.* TO 'customer_user'@'%';

-- 2. Notification Service
CREATE USER 'notification_user'@'%' IDENTIFIED BY 'notification_password';
GRANT ALL PRIVILEGES ON notification_service.* TO 'notification_user'@'%';

-- 3. Maintenance Service
CREATE USER 'maintenance_user'@'%' IDENTIFIED BY 'maintenance_password';
GRANT ALL PRIVILEGES ON maintenance_service.* TO 'maintenance_user'@'%';

-- 4. Appointment Service
CREATE USER 'appointment_user'@'%' IDENTIFIED BY 'appointment_password';
GRANT ALL PRIVILEGES ON appointment_service.* TO 'appointment_user'@'%';

-- 5. Billing Service
CREATE USER 'billing_user'@'%' IDENTIFIED BY 'billing_password';
GRANT ALL PRIVILEGES ON billing_service.* TO 'billing_user'@'%';

-- 6. Account Service
CREATE USER 'account_user'@'%' IDENTIFIED BY 'account_password';
GRANT ALL PRIVILEGES ON account_service.* TO 'account_user'@'%';

-- III. ÁP DỤNG THAY ĐỔI VÀ KHỞI TẠO BẢNG
FLUSH PRIVILEGES;

-- 1. TẠO BẢNG CHO CUSTOMER_SERVICE
USE customer_service; -- (LỖI 2 ĐÃ SỬA)

CREATE TABLE `customer` (
                            `customerID` INT NOT NULL,
                            `name` VARCHAR(45) NULL,
                            `phone` VARCHAR(10) NULL,
                            `address` VARCHAR(45) NULL,
                            `paymentInfor` VARCHAR(45) NULL,
                            PRIMARY KEY (`customerID`));

CREATE TABLE `vehicle` (
                           `vehicleID` INT NOT NULL,
                           `vin` VARCHAR(45) NULL,
                           `model` VARCHAR(45) NULL,
                           `year` VARCHAR(45) NULL,
                           `customerID` INT NOT NULL,
                           PRIMARY KEY (`vehicleID`));

ALTER TABLE `vehicle`
    ADD INDEX `fk_vehicle_customer_idx` (`customerID` ASC) VISIBLE;

ALTER TABLE `vehicle`
    ADD CONSTRAINT `fk_vehicle_customer`
        FOREIGN KEY (`customerID`)
            REFERENCES `customer` (`customerID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

-- 2. TẠO BẢNG CHO NOTIFICATION_SERVICE
USE notification_service; -- (File của bạn đã làm đúng dòng này)
CREATE TABLE `notification` (
                                `notificationID` INT NOT NULL,
                                `customerID` INT NULL,
                                `type` VARCHAR(45) NULL,
                                `message` VARCHAR(45) NULL,
                                `status` VARCHAR(100) NULL,
                                PRIMARY KEY (`notificationID`));


-- 3. TẠO BẢNG CHO MAINTENANCE_SERVICE
USE maintenance_service; -- (LỖI 2 ĐÃ SỬA)

CREATE TABLE `technician` (
                              `technicianID` INT NOT NULL,
                              `name` VARCHAR(45) NULL,
                              `specialization` VARCHAR(45) NULL,
                              `certificate` VARCHAR(45) NULL,
                              `shiftSchedule` VARCHAR(45) NULL,
                              PRIMARY KEY (`technicianID`));

CREATE TABLE `serviceRecord` (
                                 `recordID` INT NOT NULL,
                                 `technicianID` INT NULL,
                                 `vehicleID` INT NULL,
                                 `appointmentID` INT NULL,
                                 `note` VARCHAR(45) NULL,
                                 `cost` VARCHAR(45) NULL,
                                 `date` DATETIME NULL,
                                 PRIMARY KEY (`recordID`));

ALTER TABLE `serviceRecord`
    ADD INDEX `fk_record_tech_idx_idx` (`technicianID` ASC) VISIBLE;

ALTER TABLE `serviceRecord`
    ADD CONSTRAINT `fk_record_tech_idx`
        FOREIGN KEY (`technicianID`)
            REFERENCES `technician` (`technicianID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

CREATE TABLE `sparePart` (
                             `partID` INT NOT NULL,
                             `name` VARCHAR(45) NULL,
                             `description` VARCHAR(45) NULL,
                             `quantityInStock` INT NULL,
                             `minStockLevel` INT NULL,
                             `price` DOUBLE NULL,
                             PRIMARY KEY (`partID`));

CREATE TABLE `servicePartUsage` (
                                    `usageID` INT NOT NULL,
                                    `recordID` INT NULL,
                                    `partID` INT NULL,
                                    `quantityUsed` INT NULL,
                                    PRIMARY KEY (`usageID`));

ALTER TABLE `servicePartUsage`
    ADD INDEX `fk_partusage_record_idx` (`recordID` ASC) VISIBLE,
ADD INDEX `fk_partusage_sparepart_idx` (`partID` ASC) VISIBLE;

ALTER TABLE `servicePartUsage`
    ADD CONSTRAINT `fk_partusage_record`
        FOREIGN KEY (`recordID`)
            REFERENCES `serviceRecord` (`recordID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_partusage_sparepart`
  FOREIGN KEY (`partID`)
  REFERENCES `sparePart` (`partID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

-- 4. TẠO BẢNG CHO APPOINTMENT_SERVICE
USE appointment_service; -- (LỖI 2 ĐÃ SỬA)

CREATE TABLE `serviceCenter` (
                                 `serviceCenterID` INT NOT NULL,
                                 `name` VARCHAR(45) NULL,
                                 `address` VARCHAR(45) NULL,
                                 `contactInfo` VARCHAR(10) NULL,
                                 PRIMARY KEY (`serviceCenterID`));

-- (Lưu ý: Bảng serviceType phải được tạo TRƯỚC bảng appointment)
CREATE TABLE `appointment_service`.`serviceType` (
                                                     `serviceTypeID` INT NOT NULL,
                                                     `name` VARCHAR(45) NULL,
                                                     `description` VARCHAR(45) NULL,
                                                     PRIMARY KEY (`serviceTypeID`));

CREATE TABLE `appointment_service`.`appointment` (
                                                     `appointmentID` INT NOT NULL,
                                                     `servicetypeID` INT NULL,
                                                     `serviceCenterID` INT NULL,
                                                     `customerID` INT NULL,
                                                     `vehicleID` INT NULL,
                                                     `status` VARCHAR(45) NULL,
                                                     `scheduledDate` DATETIME NULL,
                                                     `createdAt` VARCHAR(45) NULL,
                                                     `notes` VARCHAR(45) NULL,
                                                     PRIMARY KEY (`appointmentID`));

ALTER TABLE `appointment_service`.`appointment`
    ADD INDEX `fk_appoint_sercen_idx` (`serviceCenterID` ASC) VISIBLE,
ADD INDEX `fk_appoint_sertype_idx` (`servicetypeID` ASC) VISIBLE;

ALTER TABLE `appointment_service`.`appointment`
    ADD CONSTRAINT `fk_appoint_sercen`
        FOREIGN KEY (`serviceCenterID`)
            REFERENCES `appointment_service`.`serviceCenter` (`serviceCenterID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_appoint_sertype`
  FOREIGN KEY (`servicetypeID`)
  REFERENCES `appointment_service`.`serviceType` (`serviceTypeID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

-- 5. TẠO BẢNG CHO BILLING_SERVICE
USE billing_service; -- (LỖI 2 ĐÃ SỬA)

CREATE TABLE `servicePackage` (
                                  `packageID` INT NOT NULL,
                                  `name` VARCHAR(45) NULL,
                                  `description` VARCHAR(45) NULL,
                                  `price` DOUBLE NULL,
                                  `duration` DATETIME NULL,
                                  PRIMARY KEY (`packageID`));

CREATE TABLE `customerServicePackage` (
                                          `subscriptionID` INT NOT NULL,
                                          `customerID` INT NULL,
                                          `packageID` INT NULL,
                                          `startDate` DATETIME NULL,
                                          `endDate` DATETIME NULL,
                                          `status` VARCHAR(45) NULL,
                                          `paymentMethod` VARCHAR(45) NULL,
                                          `autoRenew` VARCHAR(45) NULL,
                                          PRIMARY KEY (`subscriptionID`));

ALTER TABLE `customerServicePackage`
    ADD INDEX `fk_pack_cus_idx` (`packageID` ASC) VISIBLE;

ALTER TABLE `customerServicePackage`
    ADD CONSTRAINT `fk_pack_cus`
        FOREIGN KEY (`packageID`)
            REFERENCES `servicePackage` (`packageID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

CREATE TABLE `invoice` (
                           `invoiceID` INT NOT NULL,
                           `recordID` INT NULL,
                           `customerID` INT NULL,
                           `status` VARCHAR(45) NULL,
                           `amount` VARCHAR(45) NULL,
                           `date` DATETIME NULL,
                           `paymentMethod` VARCHAR(45) NULL,
                           PRIMARY KEY (`invoiceID`));

-- 6. TẠO BẢNG CHO ACCOUNT_SERVICE
USE account_service; -- (LỖI 2 ĐÃ SỬA)

CREATE TABLE `account` (
                           `accountID` INT NOT NULL,
                           `username` VARCHAR(15) NULL,
                           `password` VARCHAR(45) NULL,
                           `role` VARCHAR(45) NULL,
                           `status` VARCHAR(45) NULL,
                           PRIMARY KEY (`accountID`));