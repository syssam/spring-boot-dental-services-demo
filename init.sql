-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- 主機： mysql:3306
-- 產生時間： 2025 年 04 月 08 日 12:22
-- 伺服器版本： 8.0.41
-- PHP 版本： 8.2.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `sehs4701`
--

-- --------------------------------------------------------

--
-- 資料表結構 `appointments`
--

CREATE TABLE `appointments` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `dentist_id` bigint NOT NULL,
  `clinic_id` bigint NOT NULL,
  `treatment_type_id` bigint NOT NULL,
  `appointment_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `status` varchar(20) NOT NULL,
  `notes` text,
  `cancellation_reason` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `appointments`
--

INSERT INTO `appointments` (`id`, `user_id`, `dentist_id`, `clinic_id`, `treatment_type_id`, `appointment_date`, `start_time`, `end_time`, `status`, `notes`, `cancellation_reason`, `created_at`) VALUES
(1, 1, 2, 1, 1, '2025-04-03', '09:00:00', '09:30:00', 'CANCELLED', '', 'other', '2025-04-01 15:31:56'),
(2, 1, 2, 1, 1, '2025-04-03', '09:00:00', '09:30:00', 'CANCELLED', '', '时间冲突，无法前往', '2025-04-01 15:37:30'),
(3, 1, 2, 1, 1, '2025-04-03', '09:00:00', '09:30:00', 'CANCELLED', '', 'other', '2025-04-01 16:12:52'),
(4, 1, 2, 1, 1, '2025-04-03', '09:00:00', '09:30:00', 'CANCELLED', '', 'other', '2025-04-01 16:27:14'),
(5, 1, 1, 1, 2, '2025-04-04', '09:00:00', '09:45:00', 'CONFIRMED', '', NULL, '2025-04-01 16:29:44'),
(6, 1, 1, 1, 2, '2025-04-04', '09:00:00', '09:45:00', 'CONFIRMED', '', NULL, '2025-04-01 16:31:04'),
(7, 1, 2, 2, 1, '2025-04-05', '09:00:00', '09:30:00', 'CONFIRMED', '', NULL, '2025-04-01 16:31:59'),
(8, 1, 2, 1, 1, '2025-04-03', '09:00:00', '09:30:00', 'CONFIRMED', '', NULL, '2025-04-01 16:34:45'),
(9, 1, 2, 1, 1, '2025-04-03', '09:00:00', '09:30:00', 'CONFIRMED', '', NULL, '2025-04-01 16:38:19'),
(10, 1, 2, 1, 1, '2025-04-03', '09:00:00', '09:30:00', 'CONFIRMED', '', NULL, '2025-04-01 17:16:09'),
(11, 1, 2, 1, 1, '2025-04-03', '10:30:00', '11:00:00', 'CONFIRMED', '', NULL, '2025-04-01 17:31:08'),
(12, 1, 2, 1, 2, '2025-04-08', '09:30:00', '10:15:00', 'CONFIRMED', '', NULL, '2025-04-07 14:50:04'),
(13, 1, 1, 1, 2, '2025-04-09', '09:00:00', '09:45:00', 'CONFIRMED', '', NULL, '2025-04-07 16:27:51'),
(14, 1, 1, 1, 2, '2025-04-09', '15:30:00', '16:15:00', 'CONFIRMED', '', NULL, '2025-04-07 16:37:33');

-- --------------------------------------------------------

--
-- 資料表結構 `clinics`
--

CREATE TABLE `clinics` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `open_time` time DEFAULT NULL,
  `close_time` time DEFAULT NULL,
  `opening_hours` text,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `clinics`
--

INSERT INTO `clinics` (`id`, `name`, `address`, `phone`, `email`, `open_time`, `close_time`, `opening_hours`, `active`) VALUES
(1, 'Sham Shui Po Clinic', 'Shop 173-175, Apliu Street, Sham Shui Po', '852-12345678', 'info@hkdental.com', '10:00:00', '20:00:00', 'Monday to Friday: 10:00am - 08:00pm', 1),
(2, 'Central Clinic', 'RoomD,18F Entertainment Plaza,30 Queen\'s Road', '852-12345678', 'info@klbdental.com', '10:00:00', '20:00:00', 'Monday to Friday: 10:00am - 08:00pm', 1),
(3, 'Langham Place Clinic', 'Office 2507, Langham Place, 8 Argyle Street', '852-12345678', 'info@klbdental.com', '10:00:00', '20:00:00', 'Monday to Friday: 10:00am - 08:00pm', 1),
(4, 'Tsim Sha Tsui Clinic', 'Office 2201, Mira Place Tower A', '852-12345678', 'info@klbdental.com', '10:00:00', '20:00:00', 'Monday to Friday: 10:00am - 08:00pm', 1),
(5, 'Yau Ma Tei Clinic', '2/F, North Tower, PolyU West Kowloon Campus, 9 Hoi Ting Road, Yau Ma Tei, Kowloon', '852-12345678', 'info@klbdental.com', '10:00:00', '20:00:00', 'Monday to Friday: 10:00am - 08:00pm', 1);

-- --------------------------------------------------------

--
-- 資料表結構 `contacts`
--

CREATE TABLE `contacts` (
  `id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telephone` varchar(8) DEFAULT NULL,
  `message` varchar(500) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `contacts`
--

INSERT INTO `contacts` (`id`, `name`, `email`, `telephone`, `message`, `created_at`, `updated_at`) VALUES
(1, 'owner', 'test@codesignx.com', '12345678', 'Message', '2025-03-22 17:57:14', '2025-03-22 17:57:14'),
(2, 'owner', 'test@codesignx.com', '12345678', 'Message', '2025-03-22 17:58:01', '2025-03-22 17:58:01'),
(3, 'owner', 'test@codesignx.com', '12345678', 'Message', '2025-03-22 18:00:05', '2025-03-22 18:00:05');

-- --------------------------------------------------------

--
-- 資料表結構 `dentists`
--

CREATE TABLE `dentists` (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `specialization` varchar(255) DEFAULT NULL,
  `qualification` text,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `dentists`
--

INSERT INTO `dentists` (`id`, `name`, `specialization`, `qualification`, `active`) VALUES
(1, 'Ng Yu Tung', 'General Dentistry', 'Bachelor of Dental Surgery, The University of Hong Kong (BDS HK)\nMember of the Faculty of Dental Surgery, Royal College of Surgeons of Edinburgh (MFDS RCSEd)\nMember of the Faculty of Dental Surgery, Royal College of Physicians and Surgeons of Glasgow (MFDS RCPS [Glasg])\nMember of the Faculty of Dental Surgery, Royal College of Surgeons of England (MFDS RCS [Eng])', 1),
(2, 'Ho Lik Kwan', 'Orthodontics', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong', 1),
(3, 'Chan Suet Ying', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong', 1),
(4, 'Chan Sin Yi', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong', 1),
(5, 'Liu Ho Yi', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong', 1),
(6, 'Tsui Pak Chuen', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong\r\nMFDS RCSEd: Member of the Faculty of Dental Surgery, Royal College of Surgeons of Edinburgh\r\nMFDS RCPS (Glasg): Member of the Faculty of Dental Surgery, Royal College of Physicians and Surgeons of Glasgow', 1),
(7, 'Man Luen Him', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong', 1),
(8, 'Lam Chun Yiu', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong\r\nMDS (Paed Dent) (HK): Master of Dental Surgery in Paediatric Dentistry, The University of Hong Kong\r\nMPaed Dent RCS (Eng): Member in Paediatric Dentistry, Royal College of Surgeons of England\r\nM Paed Dent RCSEd: Member in Paediatric Dentistry, Royal College of Surgeons of Edinburgh\r\nM Paed Dent RCPS (Glasg): Member in Paediatric Dentistry, Royal College of Physicians and Surgeons of Glasgow', 1),
(9, 'Wong Wing Lam', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong\r\nMDS (Endo) (HK): Master of Dental Surgery in Endodontics, The University of Hong Kong\r\nM Endo RCSEd: Member in Endodontics, Royal College of Surgeons of Edinburgh', 1),
(10, 'Fung Cheuk Yi', 'General Dentistry', 'BDS (HK): Bachelor of Dental Surgery, The University of Hong Kong\r\nMDS (Endo) (HK): Master of Dental Surgery in Endodontics, The University of Hong Kong\r\nM Endo RCSEd: Member in Endodontics, Royal College of Surgeons of Edinburgh', 1);

-- --------------------------------------------------------

--
-- 資料表結構 `dentist_clinic`
--

CREATE TABLE `dentist_clinic` (
  `id` bigint NOT NULL,
  `dentist_id` bigint NOT NULL,
  `clinic_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `dentist_clinic`
--

INSERT INTO `dentist_clinic` (`id`, `dentist_id`, `clinic_id`) VALUES
(1, 1, 1),
(2, 2, 2);

-- --------------------------------------------------------

--
-- 資料表結構 `dentist_schedules`
--

CREATE TABLE `dentist_schedules` (
  `id` bigint NOT NULL,
  `day_of_week` enum('FRIDAY','MONDAY','SATURDAY','SUNDAY','THURSDAY','TUESDAY','WEDNESDAY') NOT NULL,
  `effective_from` date NOT NULL,
  `effective_to` date DEFAULT NULL,
  `end_time` time(6) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `start_time` time(6) NOT NULL,
  `clinic_id` bigint NOT NULL,
  `dentist_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `dentist_schedules`
--

INSERT INTO `dentist_schedules` (`id`, `day_of_week`, `effective_from`, `effective_to`, `end_time`, `active`, `start_time`, `clinic_id`, `dentist_id`) VALUES
(1, 'MONDAY', '2023-10-01', NULL, '12:00:00.000000', 1, '09:00:00.000000', 1, 1),
(2, 'MONDAY', '2023-10-01', NULL, '18:00:00.000000', 1, '14:00:00.000000', 1, 1),
(3, 'WEDNESDAY', '2023-10-01', NULL, '12:00:00.000000', 1, '09:00:00.000000', 1, 1),
(4, 'WEDNESDAY', '2023-10-01', NULL, '18:00:00.000000', 1, '14:00:00.000000', 1, 1),
(5, 'FRIDAY', '2023-10-01', NULL, '12:00:00.000000', 1, '09:00:00.000000', 1, 1),
(6, 'TUESDAY', '2023-10-01', NULL, '12:00:00.000000', 1, '09:00:00.000000', 1, 2),
(7, 'TUESDAY', '2023-10-01', NULL, '18:00:00.000000', 1, '14:00:00.000000', 1, 2),
(8, 'THURSDAY', '2023-10-01', NULL, '12:00:00.000000', 1, '09:00:00.000000', 1, 2),
(9, 'SATURDAY', '2023-10-01', NULL, '14:00:00.000000', 1, '09:00:00.000000', 2, 2),
(10, 'SUNDAY', '2023-10-01', '2023-12-31', '15:00:00.000000', 1, '10:00:00.000000', 2, 2);

-- --------------------------------------------------------

--
-- 資料表結構 `services`
--

CREATE TABLE `services` (
  `id` int NOT NULL,
  `image` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `services`
--

INSERT INTO `services` (`id`, `image`, `name`, `content`, `active`, `created_at`, `updated_at`) VALUES
(1, '1.webp', 'General Dentistry', 'From checkups to crowns, fillings, and dentures, we offer a full range of dental services with advanced technology to ensure confident, healthy smiles.\r\n', 1, '2025-04-07 07:59:45', '2025-04-07 07:59:45'),
(2, '1.webp', '\r\nCosmetic Dentistry', 'Whitening veneers gently reduce stains and brighten teeth naturally, giving you a radiant and confident smile.\r\n\r\n', 1, '2025-04-07 08:08:44', '2025-04-07 08:08:44'),
(3, '1.webp', 'Root Canal Treatment', 'With 3D CT-guided implants and custom crowns, we restore missing teeth efficiently, helping you regain a natural smile.\r\n\r\n', 1, '2025-04-07 08:08:58', '2025-04-07 08:08:58'),
(4, '1.webp', 'Orthodontics (Braces)', 'We provide traditional and invisible braces, offering customized plans to align your teeth and boost your confidence.', 1, '2025-04-07 08:09:18', '2025-04-07 08:09:18'),
(5, '1.webp', 'Pediatric Dentistry', 'Specialized care for kids with a warm, stress-free environment to support their dental health and ease parents’ concerns.', 1, '2025-04-07 08:09:43', '2025-04-07 08:09:43'),
(6, '1.webp', 'Root Canal Therapy', 'The best way to save decayed teeth and prevent gaps from extractions. We aim to preserve every precious tooth.', 1, '2025-04-07 08:10:06', '2025-04-07 08:10:06'),
(7, '1.webp', 'Dental Surgery', 'From simple extractions to wisdom tooth removal and corrective jaw surgery, our expert team has you covered.\r\n\r\n', 1, '2025-04-07 08:10:21', '2025-04-07 08:10:21'),
(8, '1.webp', 'Periodontal Treatment', 'Deep cleaning, root planing, and periodontal surgery improve gum healthy, strengthen teeth, and keep your breath fresh.', 1, '2025-04-07 08:10:35', '2025-04-07 08:10:35'),
(9, '1.webp', 'Emergency & Sunday Services', '\r\nOpen 7 days a week, including weekends and holidays, to address urgent dental needs without delays.', 1, '2025-04-07 08:10:47', '2025-04-07 08:10:47');

-- --------------------------------------------------------

--
-- 資料表結構 `treatment_types`
--

CREATE TABLE `treatment_types` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `duration_minutes` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `treatment_types`
--

INSERT INTO `treatment_types` (`id`, `name`, `description`, `duration_minutes`, `price`, `active`) VALUES
(1, '常规检查', '全面口腔检查，包括X光片', 30, 500.00, 1),
(2, '洗牙', '专业牙齿清洁和抛光', 45, 800.00, 1),
(3, '补牙', '使用复合树脂材料填充龋齿', 60, 1200.00, 1);

-- --------------------------------------------------------

--
-- 資料表結構 `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` tinyint(1) NOT NULL,
  `telephone_prefix` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `telephone`, `password`, `status`, `created_at`, `updated_at`, `active`, `telephone_prefix`) VALUES
(1, 'First Name', 'Last Name', 'sales@codesignx.com', '12345678', '$2a$10$bVDfMS.dNocL7PnpAisu1.UDz0d7vnXqcbKGbtzZrKj30RHWXs1G2', 1, '2025-03-23 10:41:12', '2025-04-07 13:34:54', 1, '852');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `clinics`
--
ALTER TABLE `clinics`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `dentists`
--
ALTER TABLE `dentists`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `dentist_clinic`
--
ALTER TABLE `dentist_clinic`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `dentist_schedules`
--
ALTER TABLE `dentist_schedules`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `treatment_types`
--
ALTER TABLE `treatment_types`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `appointments`
--
ALTER TABLE `appointments`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `clinics`
--
ALTER TABLE `clinics`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `contacts`
--
ALTER TABLE `contacts`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `dentists`
--
ALTER TABLE `dentists`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `dentist_clinic`
--
ALTER TABLE `dentist_clinic`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `dentist_schedules`
--
ALTER TABLE `dentist_schedules`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `services`
--
ALTER TABLE `services`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `treatment_types`
--
ALTER TABLE `treatment_types`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
