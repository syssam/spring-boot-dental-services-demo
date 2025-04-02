-- 禁用外键约束检查
SET FOREIGN_KEY_CHECKS=0;

-- 删除旧的patient表（如果存在）并移除相关外键约束
DROP TABLE IF EXISTS patients;

-- 删除旧的预约表（如果存在）并创建新表
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS dentist_clinic;
DROP TABLE IF EXISTS dentist_schedules;
DROP TABLE IF EXISTS treatment_types;
DROP TABLE IF EXISTS dentists;
DROP TABLE IF EXISTS clinics;

-- 用户表（如果不存在则创建）
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  telephone VARCHAR(20) NOT NULL,
  telephone_prefix VARCHAR(5) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  status BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 诊所表
CREATE TABLE clinics (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  email VARCHAR(255),
  open_time TIME,
  close_time TIME,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 牙医表
CREATE TABLE dentists (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  specialization VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(20),
  profile_image VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 牙医与诊所关联表
CREATE TABLE dentist_clinic (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  dentist_id BIGINT NOT NULL,
  clinic_id BIGINT NOT NULL
);

-- 牙医排班表
CREATE TABLE dentist_schedules (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  dentist_id BIGINT NOT NULL,
  clinic_id BIGINT NOT NULL,
  day_of_week VARCHAR(20) NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  effective_from DATE NOT NULL,
  effective_to DATE,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 治疗类型表
CREATE TABLE treatment_types (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  duration_minutes INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 预约表
CREATE TABLE appointments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  dentist_id BIGINT NOT NULL,
  clinic_id BIGINT NOT NULL,
  treatment_type_id BIGINT NOT NULL,
  appointment_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  status VARCHAR(20) NOT NULL,
  notes TEXT,
  cancellation_reason TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 重新启用外键约束检查
SET FOREIGN_KEY_CHECKS=1; 