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

-- dữ liệu mẫu
-- BƯỚC 1: THÊM DỮ LIỆU CƠ BẢN --

-- Thêm Nhà xe (Companies)
INSERT INTO `Companies` (`company_id`, `name`, `hotline`, `status`) VALUES
(1, 'Phương Trang - FUTA Bus Lines', '19006067', 'active'),
(2, 'Thành Bưởi', '19006079', 'active');

-- Thêm Địa điểm (Locations)
INSERT INTO `Locations` (`location_id`, `name`, `city`, `address`) VALUES
(1, 'Bến xe Miền Đông Mới', 'Hồ Chí Minh', '501 Hoàng Hữu Nam, P. Long Bình, TP. Thủ Đức'),
(2, 'Bến xe Đà Lạt', 'Đà Lạt', '01 Tô Hiến Thành, Phường 3, TP. Đà Lạt'),
(3, 'Bến xe Cần Thơ', 'Cần Thơ', 'QL1A, Hưng Thạnh, Cái Răng, Cần Thơ'),
(4, 'Bến xe Nước Ngầm', 'Hà Nội', 'Km số 8, Giải Phóng, Hoàng Mai, Hà Nội');

-- Thêm Người dùng (Users)
-- Lưu ý: password_hash nên được tạo bằng thuật toán hashing như BCrypt. Ở đây chỉ là placeholder.
INSERT INTO `Users` (`user_id`, `full_name`, `email`, `phone_number`, `password_hash`, `role_id`, `company_id`) VALUES
(1, 'Super Admin', 'superadmin@busbooking.com', '0900000001', '$2a$10$abc...', 1, NULL),
(2, 'Admin Phương Trang', 'admin.pt@phuongtrang.com', '0900000002', '$2a$10$abc...', 2, 1),
(3, 'Nhân viên Thành Bưởi', 'agent.tb@thanhbuoi.com', '0900000003', '$2a$10$abc...', 3, 2),
(4, 'Nguyễn Văn An', 'nguyenvana@email.com', '0912345678', '$2a$10$abc...', 4, NULL),
(5, 'Trần Thị Bích', 'tranbich@email.com', '0987654321', '$2a$10$abc...', 4, NULL);


-- BƯỚC 2: THÊM DỮ LIỆU VẬN HÀNH --

-- Thêm Tuyến đường (Routes)
INSERT INTO `Routes` (`route_id`, `company_id`, `departure_location_id`, `arrival_location_id`, `duration_minutes`) VALUES
(1, 1, 1, 2, 480), -- Phương Trang: HCM -> Đà Lạt (8 tiếng)
(2, 2, 1, 2, 450), -- Thành Bưởi: HCM -> Đà Lạt (7.5 tiếng)
(3, 1, 1, 3, 240); -- Phương Trang: HCM -> Cần Thơ (4 tiếng)

-- Thêm Xe khách (Buses)
INSERT INTO `Buses` (`bus_id`, `company_id`, `license_plate`, `seat_type`, `total_seats`) VALUES
(1, 1, '51F-123.45', 'Giường nằm 40 chỗ', 40),
(2, 1, '51F-234.56', 'Limousine 34 chỗ', 34),
(3, 2, '50F-987.65', 'Giường nằm 36 chỗ', 36);

-- Thêm Chuyến đi (Trips)
-- Lưu ý: Thời gian trong tương lai
INSERT INTO `Trips` (`trip_id`, `route_id`, `bus_id`, `departure_time`, `arrival_time`, `base_price`, `status`) VALUES
(1, 1, 1, '2025-10-20 08:00:00', '2025-10-20 16:00:00', 350000.00, 'scheduled'),
(2, 1, 2, '2025-10-20 10:00:00', '2025-10-20 18:00:00', 450000.00, 'scheduled'),
(3, 2, 3, '2025-10-21 09:00:00', '2025-10-21 16:30:00', 320000.00, 'scheduled');


-- BƯỚC 3: TẠO GHẾ CHO MỘT CHUYẾN ĐI CỤ THỂ --
-- Tạo ra 40 ghế cho chuyến đi có trip_id = 1 (Xe Phương Trang 40 chỗ)
INSERT INTO `Seats` (`trip_id`, `seat_number`, `status`) VALUES
(1, 'A01', 'available'), (1, 'A02', 'available'), (1, 'A03', 'available'), (1, 'A04', 'available'), (1, 'A05', 'available'),
(1, 'A06', 'available'), (1, 'A07', 'available'), (1, 'A08', 'available'), (1, 'A09', 'available'), (1, 'A10', 'available'),
(1, 'A11', 'available'), (1, 'A12', 'available'), (1, 'A13', 'available'), (1, 'A14', 'available'), (1, 'A15', 'available'),
(1, 'A16', 'available'), (1, 'A17', 'available'), (1, 'A18', 'available'), (1, 'A19', 'available'), (1, 'A20', 'available'),
(1, 'B01', 'available'), (1, 'B02', 'available'), (1, 'B03', 'available'), (1, 'B04', 'available'), (1, 'B05', 'available'),
(1, 'B06', 'available'), (1, 'B07', 'available'), (1, 'B08', 'available'), (1, 'B09', 'available'), (1, 'B10', 'available'),
(1, 'B11', 'available'), (1, 'B12', 'available'), (1, 'B13', 'available'), (1, 'B14', 'available'), (1, 'B15', 'available'),
(1, 'B16', 'available'), (1, 'B17', 'available'), (1, 'B18', 'available'), (1, 'B19', 'available'), (1, 'B20', 'available');


-- BƯỚC 4: MÔ PHỎNG MỘT GIAO DỊCH ĐẶT VÉ THÀNH CÔNG --
-- Hành khách Nguyễn Văn An (user_id = 4) đặt 2 ghế A05 và A06 trên chuyến đi trip_id = 1

-- 4.1. Cập nhật trạng thái 2 ghế thành 'booked'
UPDATE `Seats` SET `status` = 'booked' WHERE `trip_id` = 1 AND `seat_number` IN ('A05', 'A06');

-- 4.2. Tạo một đơn đặt vé (Booking) mới
INSERT INTO `Bookings` (`user_id`, `trip_id`, `total_amount`, `status`) VALUES
(4, 1, 700000.00, 'confirmed');

-- 4.3. Lấy booking_id vừa tạo (giả sử là 1) và seat_id của ghế A05, A06 (giả sử là 5, 6) để tạo vé
INSERT INTO `Tickets` (`booking_id`, `seat_id`, `passenger_name`, `passenger_phone`, `price`) VALUES
(1, 5, 'Nguyễn Văn An', '0912345678', 350000.00),
(1, 6, 'Vợ Nguyễn Văn An', '0912345678', 350000.00);

-- 4.4. Tạo một giao dịch thanh toán (Payment) thành công cho đơn đặt vé
INSERT INTO `Payments` (`booking_id`, `payment_method`, `amount`, `transaction_code`, `payment_time`, `status`) VALUES
(1, 'VNPAY', 700000.00, 'VNP13959913', NOW(), 'successful');


-- Kết thúc script
SELECT 'Sample data inserted successfully.' AS `status`;