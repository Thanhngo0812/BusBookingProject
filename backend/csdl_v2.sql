-- Tự động tạo cơ sở dữ liệu nếu nó chưa tồn tại.
-- Sử dụng utf8mb4 để hỗ trợ đầy đủ Unicode (ví dụ: tiếng Việt có dấu, emoji)
CREATE DATABASE IF NOT EXISTS `busbookingdb` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Sử dụng cơ sở dữ liệu
USE `busbookingdb`;

-- XÓA BẢNG CŨ (Theo thứ tự ngược, an toàn)
-- Xóa theo thứ tự ngược của sự phụ thuộc để tránh lỗi khóa ngoại.
DROP TABLE IF EXISTS `Payments`;
DROP TABLE IF EXISTS `Tickets`;
DROP TABLE IF EXISTS `DriverAssignments`; -- (Bảng mới)
DROP TABLE IF EXISTS `Bookings`;
DROP TABLE IF EXISTS `Seats`;
DROP TABLE IF EXISTS `Trips`;
DROP TABLE IF EXISTS `Buses`;
DROP TABLE IF EXISTS `Routes`;
DROP TABLE IF EXISTS `Users`;
DROP TABLE IF EXISTS `Locations`;
DROP TABLE IF EXISTS `VehicleTypes`;
DROP TABLE IF EXISTS `Companies`;
DROP TABLE IF EXISTS `Roles`;

-- =============================================== --
--            TẠO CẤU TRÚC BẢNG (Schema)            --
-- =============================================== --

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

-- 3. Bảng Locations (Địa điểm: bến xe, trạm...)
CREATE TABLE `Locations` (
  `location_id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `city` VARCHAR(100) NOT NULL,
  `address` VARCHAR(255)
) ENGINE=InnoDB;

-- 4. Bảng VehicleTypes (Loại xe)
-- Bảng này định nghĩa các loại xe (vd: "Giường nằm 40 chỗ", "Limousine 9 chỗ")
CREATE TABLE `VehicleTypes` (
  `type_id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL, 
  `total_seats` INT NOT NULL,
  `description` TEXT
) ENGINE=InnoDB;

-- 5. Bảng Users (Người dùng)
-- Bảng này chứa tất cả các đối tượng người dùng: Admin, Nhân viên, Tài xế, Hành khách
CREATE TABLE `Users` (
  `user_id` INT PRIMARY KEY AUTO_INCREMENT,
  `full_name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `phone_number` VARCHAR(15) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255),
  `role_id` INT,
  `company_id` INT, -- Gắn nhân viên/tài xế với một nhà xe
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`role_id`) REFERENCES `Roles`(`role_id`),
  FOREIGN KEY (`company_id`) REFERENCES `Companies`(`company_id`)
) ENGINE=InnoDB;

-- 6. Bảng Routes (Tuyến đường)
CREATE TABLE `Routes` (
  `route_id` INT PRIMARY KEY AUTO_INCREMENT,
  `company_id` INT, -- Tuyến đường này thuộc nhà xe nào
  `departure_location_id` INT,
  `arrival_location_id` INT,
  `duration_minutes` INT, -- Thời gian di chuyển (phút)
  FOREIGN KEY (`company_id`) REFERENCES `Companies`(`company_id`),
  FOREIGN KEY (`departure_location_id`) REFERENCES `Locations`(`location_id`),
  FOREIGN KEY (`arrival_location_id`) REFERENCES `Locations`(`location_id`)
) ENGINE=InnoDB;

-- 7. Bảng Buses (Xe khách)
-- Liên kết với loại xe (VehicleTypes)
CREATE TABLE `Buses` (
  `bus_id` INT PRIMARY KEY AUTO_INCREMENT,
  `company_id` INT,
  `vehicle_type_id` INT, -- Trỏ đến loại xe
  `license_plate` VARCHAR(20) NOT NULL UNIQUE,
  FOREIGN KEY (`company_id`) REFERENCES `Companies`(`company_id`),
  FOREIGN KEY (`vehicle_type_id`) REFERENCES `VehicleTypes`(`type_id`)
) ENGINE=InnoDB;

-- 8. Bảng Trips (Chuyến đi)
-- Đây là "chuyến" thực tế được lên lịch
CREATE TABLE `Trips` (
  `trip_id` INT PRIMARY KEY AUTO_INCREMENT,
  `route_id` INT,
  `vehicle_type_id` INT, -- Loại xe được lên lịch cho chuyến này
  `bus_id` INT,         -- Xe cụ thể sẽ chạy (có thể NULL, để gán sau)
  `departure_time` DATETIME NOT NULL,
  `arrival_time` DATETIME NOT NULL,
  `base_price` DECIMAL(10, 2) NOT NULL,
  `layout` TEXT DEFAULT NULL, -- (Tùy chọn: Sơ đồ ghế ngồi, nếu cần)
  `status` ENUM('scheduled', 'running', 'completed', 'cancelled') DEFAULT 'scheduled',
  FOREIGN KEY (`route_id`) REFERENCES `Routes`(`route_id`),
  FOREIGN KEY (`vehicle_type_id`) REFERENCES `VehicleTypes`(`type_id`),
  FOREIGN KEY (`bus_id`) REFERENCES `Buses`(`bus_id`)
) ENGINE=InnoDB;

-- 9. Bảng Seats (Ghế ngồi trên chuyến)
CREATE TABLE `Seats` (
  `seat_id` INT PRIMARY KEY AUTO_INCREMENT,
  `trip_id` INT,
  `seat_number` VARCHAR(10) NOT NULL, -- Ví dụ: "A01", "B20"
  `status` ENUM('available', 'pending', 'booked') DEFAULT 'available',
  FOREIGN KEY (`trip_id`) REFERENCES `Trips`(`trip_id`) ON DELETE CASCADE,
  UNIQUE (`trip_id`, `seat_number`) -- Một số ghế là duy nhất trên 1 chuyến
) ENGINE=InnoDB;

-- 10. Bảng Bookings (Đơn đặt vé)
CREATE TABLE `Bookings` (
  `booking_id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT, -- Người đặt vé (Passenger)
  `trip_id` INT,
  `booking_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `total_amount` DECIMAL(10, 2) NOT NULL,
  `status` ENUM('pending_payment', 'confirmed', 'cancelled') DEFAULT 'pending_payment',
  `created_by` INT, -- Người tạo đơn (có thể là TicketAgent hoặc chính Passenger)
  FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`),
  FOREIGN KEY (`trip_id`) REFERENCES `Trips`(`trip_id`),
  FOREIGN KEY (`created_by`) REFERENCES `Users`(`user_id`)
) ENGINE=InnoDB;

-- 11. Bảng Tickets (Vé xe)
-- Vé là chi tiết của một Đơn đặt (Booking)
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
  UNIQUE (`seat_id`) -- Rất quan trọng: 1 ghế chỉ được bán 1 lần
) ENGINE=InnoDB;

-- 12. Bảng Payments (Thanh toán)
CREATE TABLE `Payments` (
  `payment_id` INT PRIMARY KEY AUTO_INCREMENT,
  `booking_id` INT,
  `payment_method` VARCHAR(50),
  `amount` DECIMAL(10, 2) NOT NULL,
  `transaction_code` VARCHAR(100), -- Mã giao dịch từ bên thứ 3 (VNPAY, Momo)
  `payment_time` DATETIME,
  `status` ENUM('pending', 'successful', 'failed') DEFAULT 'pending',
  FOREIGN KEY (`booking_id`) REFERENCES `Bookings`(`booking_id`)
) ENGINE=InnoDB;

-- 13. BẢNG MỚI: DriverAssignments (Phân công Tài xế)
-- Bảng này liên kết 'Trips' (Chuyến đi) với 'Users' (Tài xế)
CREATE TABLE `DriverAssignments` (
  `assignment_id` INT PRIMARY KEY AUTO_INCREMENT,
  `trip_id` INT NOT NULL,
  `driver_id` INT NOT NULL, -- (Chính là `user_id` của tài xế)
  `assignment_role` VARCHAR(50) DEFAULT 'Main Driver', -- (Tùy chọn: 'Tài chính', 'Tài phụ')
  
  FOREIGN KEY (`trip_id`) REFERENCES `Trips`(`trip_id`) ON DELETE CASCADE,
  FOREIGN KEY (`driver_id`) REFERENCES `Users`(`user_id`),
  
  -- Đảm bảo 1 tài xế không bị gán 2 lần vào 1 chuyến
  UNIQUE (`trip_id`, `driver_id`)
) ENGINE=InnoDB;


-- =============================================== --
--            THÊM DỮ LIỆU MẪU (Sample Data)         --
-- =============================================== --

-- 1. Thêm Roles (ĐÃ THÊM 'Driver')
INSERT INTO `Roles` (`role_name`) VALUES 
('SuperAdmin'), ('CompanyAdmin'), ('TicketAgent'), ('Passenger'), ('Driver');

-- 2. Thêm Companies
INSERT INTO `Companies` (`company_id`, `name`, `hotline`, `status`) VALUES 
(1, 'Phương Trang - FUTA Bus Lines', '19006067', 'active'), 
(2, 'Thành Bưởi', '19006079', 'active');

-- 3. Thêm Locations
INSERT INTO `Locations` (`location_id`, `name`, `city`, `address`) VALUES 
(1, 'Bến xe Miền Đông Mới', 'Hồ Chí Minh', '501 Hoàng Hữu Nam...'), 
(2, 'Bến xe Đà Lạt', 'Đà Lạt', '01 Tô Hiến Thành...');

-- 4. Thêm VehicleTypes
INSERT INTO `VehicleTypes` (`type_id`, `name`, `total_seats`) VALUES
(1, 'Giường nằm 40 chỗ', 40),
(2, 'Limousine 34 chỗ', 34),
(3, 'Giường nằm 36 chỗ', 36);

-- 5. Thêm Users (Hành khách và Tài xế)
-- (Giả sử role_id: 4 = Passenger, 5 = Driver)
INSERT INTO `Users` (`user_id`, `full_name`, `email`, `phone_number`, `password_hash`, `role_id`, `company_id`) VALUES 
(1, 'Nguyễn Văn An', 'nguyenvana@email.com', '0912345678', '$2a$10$abc...', 4, NULL), -- Hành khách, không thuộc nhà xe
(2, 'Nguyễn Văn Tài', 'taixe01@phuongtrang.com', '0909111222', '$2a$10$def...', 5, 1); -- Tài xế, thuộc Phương Trang

-- 6. Thêm Routes
INSERT INTO `Routes` (`route_id`, `company_id`, `departure_location_id`, `arrival_location_id`, `duration_minutes`) VALUES 
(1, 1, 1, 2, 480); -- Tuyến Sài Gòn -> Đà Lạt của Phương Trang

-- 7. Thêm Buses
INSERT INTO `Buses` (`bus_id`, `company_id`, `vehicle_type_id`, `license_plate`) VALUES
(1, 1, 1, '51F-123.45'), -- Xe Phương Trang, loại giường nằm 40 chỗ
(2, 1, 2, '51F-234.56'); -- Xe Phương Trang, loại Limousine 34 chỗ

-- 8. Thêm Trips
INSERT INTO `Trips` (`trip_id`, `route_id`, `vehicle_type_id`, `bus_id`, `departure_time`, `arrival_time`, `base_price`, `status`) VALUES
(1, 1, 1, 1, '2025-11-20 08:00:00', '2025-11-20 16:00:00', 350000.00, 'scheduled'),
(2, 1, 2, NULL, '2025-11-20 10:00:00', '2025-11-20 18:00:00', 450000.00, 'scheduled');

-- 9. Thêm Seats (Tạo ghế tự động cho chuyến 1)
-- (Đây là ví dụ rút gọn, backend sẽ sinh 40 ghế)
INSERT INTO `Seats` (`trip_id`, `seat_number`) VALUES
(1, 'A01'), (1, 'A02'), (1, 'A03'), (1, 'A04'), (1, 'A05'), (1, 'A06');
-- ... (Thêm B01...B20)

-- 10. (MỚI) Phân công tài xế cho chuyến đi
-- Gán tài xế 'Nguyễn Văn Tài' (user_id=2) cho chuyến đi (trip_id=1)
INSERT INTO `DriverAssignments` (`trip_id`, `driver_id`) VALUES (1, 2);

-- 11. Mô phỏng giao dịch đặt vé (cho hành khách user_id=1, chuyến trip_id=1)
UPDATE `Seats` SET `status` = 'booked' WHERE `trip_id` = 1 AND `seat_number` IN ('A05', 'A06');
INSERT INTO `Bookings` (`booking_id`, `user_id`, `trip_id`, `total_amount`, `status`, `created_by`) VALUES (1, 1, 1, 700000.00, 'confirmed', 1);

-- 12. Thêm Tickets
-- (Giả sử A05 có seat_id=5, A06 có seat_id=6)
INSERT INTO `Tickets` (`booking_id`, `seat_id`, `passenger_name`, `price`) VALUES
(1, 5, 'Nguyễn Văn An', 350000.00),
(1, 6, 'Vợ Nguyễn Văn An', 350000.00);

-- 13. Thêm Payments
INSERT INTO `Payments` (`booking_id`, `payment_method`, `amount`, `status`) VALUES 
(1, 'VNPAY', 700000.00, 'successful');


SELECT 'Database and sample data created successfully (with DriverAssignments).' AS `status`;