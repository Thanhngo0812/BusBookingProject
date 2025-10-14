-- Tạo cơ sở dữ liệu mới nếu nó chưa tồn tại
CREATE DATABASE IF NOT EXISTS `busbookingdb` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Sử dụng cơ sở dữ liệu vừa tạo
USE `busbookingdb`;

-- Xóa các bảng cũ theo thứ tự ngược lại của sự phụ thuộc để tránh lỗi khóa ngoại
DROP TABLE IF EXISTS `Payments`;
DROP TABLE IF EXISTS `Tickets`;
DROP TABLE IF EXISTS `Bookings`;
DROP TABLE IF EXISTS `Seats`;
DROP TABLE IF EXISTS `Trips`;
DROP TABLE IF EXISTS `Buses`;
DROP TABLE IF EXISTS `Routes`;
DROP TABLE IF EXISTS `Users`;
DROP TABLE IF EXISTS `Locations`;
DROP TABLE IF EXISTS `Companies`;
DROP TABLE IF EXISTS `Roles`;

-- Bắt đầu tạo lại các bảng theo đúng thứ tự --

-- 1. Bảng Roles (Vai trò)
CREATE TABLE `Roles` (
  `role_id` INT PRIMARY KEY AUTO_INCREMENT,
  `role_name` VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- 2. Bảng Companies (Nhà xe)
CREATE TABLE `Companies` (
  `company_id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `logo_url` VARCHAR(255),
  `hotline` VARCHAR(15),
  `description` TEXT,
  `status` ENUM('active', 'inactive') DEFAULT 'active'
) ENGINE=InnoDB;

-- 3. Bảng Locations (Địa điểm)
CREATE TABLE `Locations` (
  `location_id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `city` VARCHAR(100) NOT NULL,
  `address` VARCHAR(255)
) ENGINE=InnoDB;

-- 4. Bảng Users (Người dùng)
CREATE TABLE `Users` (
  `user_id` INT PRIMARY KEY AUTO_INCREMENT,
  `full_name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `phone_number` VARCHAR(15) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255),
  `role_id` INT,
  `company_id` INT, -- Mặc định là NULL cho hành khách và SuperAdmin
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`role_id`) REFERENCES `Roles`(`role_id`),
  FOREIGN KEY (`company_id`) REFERENCES `Companies`(`company_id`)
) ENGINE=InnoDB;

-- 5. Bảng Routes (Tuyến đường)
CREATE TABLE `Routes` (
  `route_id` INT PRIMARY KEY AUTO_INCREMENT,
  `company_id` INT,
  `departure_location_id` INT,
  `arrival_location_id` INT,
  `duration_minutes` INT,
  FOREIGN KEY (`company_id`) REFERENCES `Companies`(`company_id`),
  FOREIGN KEY (`departure_location_id`) REFERENCES `Locations`(`location_id`),
  FOREIGN KEY (`arrival_location_id`) REFERENCES `Locations`(`location_id`)
) ENGINE=InnoDB;

-- 6. Bảng Buses (Xe khách)
CREATE TABLE `Buses` (
  `bus_id` INT PRIMARY KEY AUTO_INCREMENT,
  `company_id` INT,
  `license_plate` VARCHAR(20) NOT NULL UNIQUE,
  `seat_type` VARCHAR(50),
  `total_seats` INT NOT NULL,
  FOREIGN KEY (`company_id`) REFERENCES `Companies`(`company_id`)
) ENGINE=InnoDB;

-- 7. Bảng Trips (Chuyến đi)
CREATE TABLE `Trips` (
  `trip_id` INT PRIMARY KEY AUTO_INCREMENT,
  `route_id` INT,
  `bus_id` INT,
  `departure_time` DATETIME NOT NULL,
  `arrival_time` DATETIME NOT NULL,
  `base_price` DECIMAL(10, 2) NOT NULL,
  `status` ENUM('scheduled', 'running', 'completed', 'cancelled') DEFAULT 'scheduled',
  FOREIGN KEY (`route_id`) REFERENCES `Routes`(`route_id`),
  FOREIGN KEY (`bus_id`) REFERENCES `Buses`(`bus_id`)
) ENGINE=InnoDB;

-- 8. Bảng Seats (Ghế ngồi trên chuyến)
CREATE TABLE `Seats` (
  `seat_id` INT PRIMARY KEY AUTO_INCREMENT,
  `trip_id` INT,
  `seat_number` VARCHAR(10) NOT NULL,
  `status` ENUM('available', 'pending', 'booked') DEFAULT 'available', -- Thêm 'pending' để giữ chỗ
  FOREIGN KEY (`trip_id`) REFERENCES `Trips`(`trip_id`) ON DELETE CASCADE,
  UNIQUE (`trip_id`, `seat_number`) -- Đảm bảo mỗi ghế là duy nhất trong 1 chuyến
) ENGINE=InnoDB;

-- 9. Bảng Bookings (Đơn đặt vé)
CREATE TABLE `Bookings` (
  `booking_id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT, -- Cho phép NULL cho khách vãng lai (hoặc dùng phương pháp tạo tài khoản guest)
  `trip_id` INT,
  `booking_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `total_amount` DECIMAL(10, 2) NOT NULL,
  `status` ENUM('pending_payment', 'confirmed', 'cancelled') DEFAULT 'pending_payment',
  `created_by` INT, -- ID của nhân viên nếu nhân viên đặt hộ
  FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`),
  FOREIGN KEY (`trip_id`) REFERENCES `Trips`(`trip_id`),
  FOREIGN KEY (`created_by`) REFERENCES `Users`(`user_id`)
) ENGINE=InnoDB;

-- 10. Bảng Tickets (Vé xe)
CREATE TABLE `Tickets` (
  `ticket_id` INT PRIMARY KEY AUTO_INCREMENT,
  `booking_id` INT,
  `seat_id` INT,
  `passenger_name` VARCHAR(100) NOT NULL,
  `passenger_phone` VARCHAR(15),
  `price` DECIMAL(10, 2) NOT NULL,
  `qr_code_url` VARCHAR(255),
  `status` ENUM('valid', 'cancelled', 'checked_in') DEFAULT 'valid',
  FOREIGN KEY (`booking_id`) REFERENCES `Bookings`(`booking_id`) ON DELETE CASCADE,
  FOREIGN KEY (`seat_id`) REFERENCES `Seats`(`seat_id`),
  UNIQUE (`seat_id`) -- Mỗi ghế chỉ được gán cho 1 vé
) ENGINE=InnoDB;

-- 11. Bảng Payments (Thanh toán)
CREATE TABLE `Payments` (
  `payment_id` INT PRIMARY KEY AUTO_INCREMENT,
  `booking_id` INT,
  `payment_method` VARCHAR(50),
  `amount` DECIMAL(10, 2) NOT NULL,
  `transaction_code` VARCHAR(100),
  `payment_time` DATETIME,
  `status` ENUM('pending', 'successful', 'failed') DEFAULT 'pending',
  FOREIGN KEY (`booking_id`) REFERENCES `Bookings`(`booking_id`)
) ENGINE=InnoDB;

-- Thêm dữ liệu ban đầu cho bảng Roles
INSERT INTO `Roles` (`role_name`) VALUES 
('SuperAdmin'), 
('CompanyAdmin'), 
('TicketAgent'), 
('Passenger');

-- Kết thúc script
SELECT 'Database and tables created successfully.' AS status;

--dữ liệu mẫu