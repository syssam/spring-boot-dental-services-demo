-- 设置SQL模式，允许处理更灵活的SQL语法
SET sql_mode = '';

-- 插入默认管理员账户（如果不存在）
INSERT INTO users (email, password, first_name, last_name, telephone_prefix, telephone, active, status) 
SELECT 'admin@example.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'Admin', 'User', '852', '12345678', true, true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');

-- 插入测试用户（如果不存在）
INSERT INTO users (email, password, first_name, last_name, telephone_prefix, telephone, active, status)
SELECT 'user@example.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', '测试', '用户', '852', '87654321', true, true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user@example.com');

-- 插入示例诊所
INSERT INTO clinics (name, address, city, phone, email, active)
SELECT '香港牙科诊所', '香港中环皇后大道中100号', '香港', '852-12345678', 'info@hkdental.com', true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM clinics WHERE name = '香港牙科诊所');

INSERT INTO clinics (name, address, city, phone, email, active)
SELECT '九龙湾牙科中心', '九龙湾宏照道33号', '香港', '852-23456789', 'info@klbdental.com', true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM clinics WHERE name = '九龙湾牙科中心');

-- 插入示例牙医
INSERT INTO dentists (first_name, last_name, specialization, email, phone, active)
SELECT '张', '医生', '普通牙科', 'zhang@hkdental.com', '852-98765432', true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM dentists WHERE email = 'zhang@hkdental.com');

INSERT INTO dentists (first_name, last_name, specialization, email, phone, active)
SELECT '李', '医生', '正畸牙科', 'li@hkdental.com', '852-87654321', true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM dentists WHERE email = 'li@hkdental.com');

-- 插入治疗类型
INSERT INTO treatment_types (name, description, duration_minutes, price, active)
SELECT '常规检查', '全面口腔检查，包括X光片', 30, 500.00, true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment_types WHERE name = '常规检查');

INSERT INTO treatment_types (name, description, duration_minutes, price, active)
SELECT '洗牙', '专业牙齿清洁和抛光', 45, 800.00, true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment_types WHERE name = '洗牙');

INSERT INTO treatment_types (name, description, duration_minutes, price, active)
SELECT '补牙', '使用复合树脂材料填充龋齿', 60, 1200.00, true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment_types WHERE name = '补牙');

-- 关联牙医和诊所
INSERT INTO dentist_clinic (dentist_id, clinic_id)
SELECT d.id, c.id
FROM dentists d, clinics c
WHERE d.email = 'zhang@hkdental.com' AND c.name = '香港牙科诊所'
AND NOT EXISTS (
    SELECT 1 FROM dentist_clinic dc
    WHERE dc.dentist_id = d.id AND dc.clinic_id = c.id
);

INSERT INTO dentist_clinic (dentist_id, clinic_id)
SELECT d.id, c.id
FROM dentists d, clinics c
WHERE d.email = 'li@hkdental.com' AND c.name = '九龙湾牙科中心'
AND NOT EXISTS (
    SELECT 1 FROM dentist_clinic dc
    WHERE dc.dentist_id = d.id AND dc.clinic_id = c.id
); 