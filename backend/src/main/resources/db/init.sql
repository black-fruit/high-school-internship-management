-- 高校实习管理系统数据库脚本
-- 数据库: test_q912_1

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    user_type TINYINT NOT NULL COMMENT '用户类型: 1-学生 2-教师 3-企业 4-管理员',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    resource_type VARCHAR(20) COMMENT '资源类型: menu-菜单 button-按钮 api-接口',
    resource_path VARCHAR(255) COMMENT '资源路径',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 学生信息表
CREATE TABLE IF NOT EXISTS student_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    student_no VARCHAR(50) COMMENT '学号',
    class_name VARCHAR(100) COMMENT '班级',
    major VARCHAR(100) COMMENT '专业',
    college VARCHAR(100) COMMENT '学院',
    teacher_id BIGINT COMMENT '指导教师ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- 教师信息表
CREATE TABLE IF NOT EXISTS teacher_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    teacher_no VARCHAR(50) COMMENT '工号',
    college VARCHAR(100) COMMENT '学院',
    department VARCHAR(100) COMMENT '系部',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师信息表';

-- 企业表
CREATE TABLE IF NOT EXISTS enterprise (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '企业ID',
    enterprise_name VARCHAR(200) NOT NULL COMMENT '企业名称',
    credit_code VARCHAR(50) COMMENT '统一社会信用代码',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(255) COMMENT '地址',
    description TEXT COMMENT '企业简介',
    audit_status TINYINT DEFAULT 0 COMMENT '审核状态: 0-待审核 1-审核通过 2-审核拒绝',
    audit_by BIGINT COMMENT '审核人ID',
    audit_remark VARCHAR(255) COMMENT '审核备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业表';

-- 企业用户关联表
CREATE TABLE IF NOT EXISTS enterprise_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    enterprise_id BIGINT NOT NULL COMMENT '企业ID',
    position VARCHAR(100) COMMENT '职位',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业用户关联表';

-- 实习计划表
CREATE TABLE IF NOT EXISTS internship_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    plan_name VARCHAR(200) NOT NULL COMMENT '计划名称',
    college_id BIGINT COMMENT '学院ID',
    college_name VARCHAR(100) COMMENT '学院名称',
    major VARCHAR(100) COMMENT '专业',
    description TEXT COMMENT '计划描述',
    requirements TEXT COMMENT '实习要求',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-草稿 1-进行中 2-已结束',
    create_by BIGINT COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实习计划表';

-- 实习岗位表
CREATE TABLE IF NOT EXISTS internship_position (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '岗位ID',
    enterprise_id BIGINT NOT NULL COMMENT '企业ID',
    enterprise_name VARCHAR(200) COMMENT '企业名称',
    plan_id BIGINT COMMENT '实习计划ID',
    position_name VARCHAR(200) NOT NULL COMMENT '岗位名称',
    description TEXT COMMENT '岗位描述',
    requirements TEXT COMMENT '岗位要求',
    major_required VARCHAR(255) COMMENT '专业要求',
    recruit_count INT DEFAULT 1 COMMENT '招聘人数',
    applied_count INT DEFAULT 0 COMMENT '已申请人数',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-关闭 1-开放',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实习岗位表';

-- 实习申请表
CREATE TABLE IF NOT EXISTS internship_application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_name VARCHAR(50) COMMENT '学生姓名',
    position_id BIGINT NOT NULL COMMENT '岗位ID',
    position_name VARCHAR(200) COMMENT '岗位名称',
    enterprise_id BIGINT COMMENT '企业ID',
    enterprise_name VARCHAR(200) COMMENT '企业名称',
    resume TEXT COMMENT '简历/申请说明',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-待审核 1-已通过 2-已拒绝',
    reject_reason VARCHAR(255) COMMENT '拒绝原因',
    teacher_id BIGINT COMMENT '指导教师ID',
    teacher_name VARCHAR(50) COMMENT '指导教师姓名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实习申请表';

-- 周报表
CREATE TABLE IF NOT EXISTS weekly_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '周报ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_name VARCHAR(50) COMMENT '学生姓名',
    application_id BIGINT NOT NULL COMMENT '实习申请ID',
    week_number INT NOT NULL COMMENT '周次',
    content TEXT NOT NULL COMMENT '周报内容',
    teacher_comment TEXT COMMENT '教师评语',
    teacher_id BIGINT COMMENT '教师ID',
    comment_time DATETIME COMMENT '评阅时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周报表';

-- 实习报告表
CREATE TABLE IF NOT EXISTS internship_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报告ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_name VARCHAR(50) COMMENT '学生姓名',
    application_id BIGINT NOT NULL COMMENT '实习申请ID',
    title VARCHAR(200) COMMENT '报告标题',
    content LONGTEXT COMMENT '报告内容',
    attachment VARCHAR(500) COMMENT '附件路径',
    submit_time DATETIME COMMENT '提交时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实习报告表';

-- 实习成绩表
CREATE TABLE IF NOT EXISTS internship_score (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_name VARCHAR(50) COMMENT '学生姓名',
    application_id BIGINT NOT NULL COMMENT '实习申请ID',
    teacher_score DECIMAL(5,2) COMMENT '教师评分',
    enterprise_score DECIMAL(5,2) COMMENT '企业评分',
    total_score DECIMAL(5,2) COMMENT '总成绩',
    teacher_comment TEXT COMMENT '教师评语',
    enterprise_comment TEXT COMMENT '企业评语',
    teacher_id BIGINT COMMENT '教师ID',
    enterprise_user_id BIGINT COMMENT '企业用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实习成绩表';

-- 初始化角色数据
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('学生', 'student', '学生角色', 1),
('教师', 'teacher', '教师角色', 1),
('企业', 'enterprise', '企业角色', 1),
('管理员', 'admin', '系统管理员', 1);

-- 初始化权限数据
INSERT INTO sys_permission (permission_name, permission_code, resource_type, resource_path, parent_id, sort) VALUES
('用户管理', 'user:manage', 'menu', '/user', 0, 1),
('用户列表', 'user:list', 'button', '/user/list', 1, 1),
('角色管理', 'role:manage', 'menu', '/role', 0, 2),
('企业管理', 'enterprise:manage', 'menu', '/enterprise', 0, 3),
('企业审核', 'enterprise:audit', 'button', '/enterprise/audit', 3, 1),
('实习计划管理', 'plan:manage', 'menu', '/plan', 0, 4),
('实习岗位管理', 'position:manage', 'menu', '/position', 0, 5),
('实习申请管理', 'application:manage', 'menu', '/application', 0, 6),
('周报管理', 'weekly:manage', 'menu', '/weekly', 0, 7),
('成绩管理', 'score:manage', 'menu', '/score', 0, 8);

-- 初始化角色权限关联
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM sys_role r, sys_permission p WHERE r.role_code = 'admin';

-- 初始化管理员账号 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, user_type, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', 4, 1);

-- 关联管理员角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM sys_user u, sys_role r WHERE u.username = 'admin' AND r.role_code = 'admin';

-- 初始化测试企业
INSERT INTO enterprise (enterprise_name, credit_code, contact_person, contact_phone, email, address, description, audit_status) VALUES
('测试科技有限公司', '91110000000000000X', '张经理', '13800138000', 'test@test.com', '北京市海淀区', '一家专注于软件开发的高新技术企业', 1);

-- 初始化测试实习计划
INSERT INTO internship_plan (plan_name, college_name, major, description, requirements, start_date, end_date, status) VALUES
('2024年春季实习计划', '计算机学院', '软件工程', '2024年春季学期实习计划', '完成至少3个月的实习工作', '2024-03-01', '2024-06-30', 1);

-- 初始化测试岗位
INSERT INTO internship_position (enterprise_id, enterprise_name, plan_id, position_name, description, requirements, major_required, recruit_count, applied_count, start_date, end_date, status)
SELECT e.id, e.enterprise_name, p.id, 'Java开发实习生', '参与公司Java项目开发', '熟悉Java编程，了解Spring框架', '软件工程,计算机科学', 5, 0, '2024-03-01', '2024-06-30', 1
FROM enterprise e, internship_plan p WHERE e.enterprise_name = '测试科技有限公司' AND p.plan_name = '2024年春季实习计划';

-- 初始化测试教师账号 (密码: teacher123)
INSERT INTO sys_user (username, password, real_name, phone, email, user_type, status) VALUES
('teacher', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李老师', '13900139000', 'teacher@test.com', 2, 1);

INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM sys_user u, sys_role r WHERE u.username = 'teacher' AND r.role_code = 'teacher';

INSERT INTO teacher_info (user_id, teacher_no, college, department)
SELECT u.id, 'T2024001', '计算机学院', '软件工程系' FROM sys_user u WHERE u.username = 'teacher';

-- 初始化测试学生账号 (密码: student123)
INSERT INTO sys_user (username, password, real_name, phone, email, user_type, status) VALUES
('student', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王同学', '13700137000', 'student@test.com', 1, 1);

INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM sys_user u, sys_role r WHERE u.username = 'student' AND r.role_code = 'student';

INSERT INTO student_info (user_id, student_no, class_name, major, college, teacher_id)
SELECT u.id, 'S2024001', '软件工程2021级1班', '软件工程', '计算机学院', t.id 
FROM sys_user u, teacher_info t WHERE u.username = 'student' AND t.teacher_no = 'T2024001';

-- 初始化测试企业用户账号 (密码: enterprise123)
INSERT INTO sys_user (username, password, real_name, phone, email, user_type, status) VALUES
('enterprise', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张经理', '13800138001', 'enterprise@test.com', 3, 1);

INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM sys_user u, sys_role r WHERE u.username = 'enterprise' AND r.role_code = 'enterprise';

INSERT INTO enterprise_user (user_id, enterprise_id, position)
SELECT u.id, e.id, '人力资源经理' FROM sys_user u, enterprise e WHERE u.username = 'enterprise' AND e.enterprise_name = '测试科技有限公司';
