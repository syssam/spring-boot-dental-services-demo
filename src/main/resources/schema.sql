-- Disable foreign key constraint checks
SET FOREIGN_KEY_CHECKS = 0;

-- Delete old patient table (if exists) and remove related foreign key constraints
DROP TABLE IF EXISTS patient;

-- Delete old appointment table (if exists) and create new table
DROP TABLE IF EXISTS appointment;

-- Enable foreign key constraint checks
SET FOREIGN_KEY_CHECKS = 1;

-- Users table (create if not exists)
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    telephone_prefix VARCHAR(4) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Clinics table
CREATE TABLE IF NOT EXISTS clinic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    description TEXT,
    opening_hours TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Dentists table
CREATE TABLE IF NOT EXISTS dentist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    specialization VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Dentist-Clinic association table
CREATE TABLE IF NOT EXISTS dentist_clinic (
    dentist_id BIGINT NOT NULL,
    clinic_id BIGINT NOT NULL,
    PRIMARY KEY (dentist_id, clinic_id),
    FOREIGN KEY (dentist_id) REFERENCES dentist(id),
    FOREIGN KEY (clinic_id) REFERENCES clinic(id)
);

-- Dentist schedule table
CREATE TABLE IF NOT EXISTS dentist_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dentist_id BIGINT NOT NULL,
    clinic_id BIGINT NOT NULL,
    day_of_week INT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dentist_id) REFERENCES dentist(id),
    FOREIGN KEY (clinic_id) REFERENCES clinic(id)
);

-- Treatment types table
CREATE TABLE IF NOT EXISTS treatment_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    dentist_id BIGINT NOT NULL,
    clinic_id BIGINT NOT NULL,
    treatment_type_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (dentist_id) REFERENCES dentist(id),
    FOREIGN KEY (clinic_id) REFERENCES clinic(id),
    FOREIGN KEY (treatment_type_id) REFERENCES treatment_type(id)
);

-- Re-enable foreign key constraint checks
SET FOREIGN_KEY_CHECKS = 1; 