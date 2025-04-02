-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- 主機： mysql:3306
-- 產生時間： 2025 年 04 月 02 日 14:43
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
(11, 1, 2, 1, 1, '2025-04-03', '10:30:00', '11:00:00', 'CONFIRMED', '', NULL, '2025-04-01 17:31:08');

-- --------------------------------------------------------

--
-- 資料表結構 `clinics`
--

CREATE TABLE `clinics` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `open_time` time DEFAULT NULL,
  `close_time` time DEFAULT NULL,
  `description` text,
  `telephone` int NOT NULL,
  `district` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `clinics`
--

INSERT INTO `clinics` (`id`, `name`, `address`, `city`, `phone`, `email`, `open_time`, `close_time`, `description`, `telephone`, `district`, `active`) VALUES
(1, '香港牙科诊所', '香港中环皇后大道中100号', '香港', '852-12345678', 'info@hkdental.com', NULL, NULL, NULL, 0, '', 1),
(2, '九龙湾牙科中心', '九龙湾宏照道33号', '香港', '852-23456789', 'info@klbdental.com', NULL, NULL, NULL, 0, '', 1);

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
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `specialization` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 傾印資料表的資料 `dentists`
--

INSERT INTO `dentists` (`id`, `first_name`, `last_name`, `specialization`, `email`, `phone`, `profile_image`, `active`) VALUES
(1, '张', '医生', '普通牙科', 'zhang@hkdental.com', '852-98765432', NULL, 1),
(2, '李', '医生', '正畸牙科', 'li@hkdental.com', '852-87654321', NULL, 1);

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
(1, 'First Name', 'Last Name', 'sales@codesignx.com', '12345678', '$2a$10$bVDfMS.dNocL7PnpAisu1.UDz0d7vnXqcbKGbtzZrKj30RHWXs1G2', 1, '2025-03-23 10:41:12', '2025-03-31 13:05:13', 1, '852');

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `clinics`
--
ALTER TABLE `clinics`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `contacts`
--
ALTER TABLE `contacts`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `dentists`
--
ALTER TABLE `dentists`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

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
