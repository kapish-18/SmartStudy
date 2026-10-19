-- ============================================
-- SmartStudy Database Schema
-- Intelligent Academic Planning & Progress Management System
-- ============================================

CREATE DATABASE IF NOT EXISTS smartstudy;
USE smartstudy;

-- ============================================
-- Users Table (students and teachers)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'TEACHER') NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Subjects Table
-- ============================================
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    course_code VARCHAR(20),
    credits INT DEFAULT 0,
    target_marks DOUBLE DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- Tasks Table
-- ============================================
CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    priority INT DEFAULT 3,
    deadline DATE,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
    estimated_minutes INT DEFAULT 60,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE SET NULL
);

-- ============================================
-- Exams Table
-- ============================================
CREATE TABLE IF NOT EXISTS exams (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    exam_name VARCHAR(200) NOT NULL,
    exam_date DATE NOT NULL,
    target_marks DOUBLE DEFAULT 0.0,
    marks_obtained DOUBLE DEFAULT -1.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- ============================================
-- Assignments Table
-- ============================================
CREATE TABLE IF NOT EXISTS assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    deadline DATE,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
    marks DOUBLE DEFAULT -1.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- ============================================
-- Study Sessions Table
-- ============================================
CREATE TABLE IF NOT EXISTS study_sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    topic VARCHAR(200),
    duration_minutes INT NOT NULL DEFAULT 0,
    session_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- ============================================
-- Sample Data for Testing
-- ============================================

-- Insert sample users
-- Credentials match JavaFX demo buttons and web portal login.jsp
INSERT INTO users (name, email, password, role) VALUES
    ('Alex Johnson', 'alex@smartstudy.edu', 'pass123', 'STUDENT'),
    ('Prof. Sharma', 'sharma@smartstudy.edu', 'pass123', 'TEACHER'),
    ('Rohan Patel', 'rohan@smartstudy.edu', 'pass123', 'STUDENT'),
    ('Sneha Reddy', 'sneha@smartstudy.edu', 'pass123', 'STUDENT');

-- Insert sample subjects for student_id = 1 (Alex Johnson)
INSERT INTO subjects (student_id, name, course_code, credits, target_marks) VALUES
    (1, 'Database Management Systems', 'BCSE302L', 4, 85.0),
    (1, 'Design and Analysis of Algorithms', 'BCSE303L', 4, 80.0),
    (1, 'Java Programming', 'BCSE203E', 3, 90.0),
    (1, 'Computer Networks', 'BCSE304L', 3, 75.0);

-- Insert sample subjects for student_id = 3 (Rohan Patel)
INSERT INTO subjects (student_id, name, course_code, credits, target_marks) VALUES
    (3, 'Operating Systems', 'BCSE305L', 4, 80.0),
    (3, 'Theory of Computation', 'BCSE306L', 3, 75.0),
    (3, 'Java Programming', 'BCSE203E', 3, 85.0);

-- Insert sample subjects for student_id = 4 (Sneha Reddy)
INSERT INTO subjects (student_id, name, course_code, credits, target_marks) VALUES
    (4, 'Software Engineering', 'BCSE307L', 3, 90.0),
    (4, 'Database Management Systems', 'BCSE302L', 4, 88.0),
    (4, 'Cloud Computing', 'BCSE308L', 3, 82.0);

-- Insert sample tasks
INSERT INTO tasks (student_id, subject_id, title, description, priority, deadline, status, estimated_minutes) VALUES
    (1, 1, 'Complete ER Diagram', 'Design ER diagram for hospital management system', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'PENDING', 120),
    (1, 2, 'Solve DP Problems', 'Complete 10 dynamic programming problems from textbook', 2, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'IN_PROGRESS', 180),
    (1, 3, 'SmartStudy Project', 'Build JavaFX application for course project', 1, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'IN_PROGRESS', 600),
    (1, 4, 'Network Lab Report', 'Write lab report on TCP/UDP socket programming', 3, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'PENDING', 90),
    (1, 1, 'Read Normalization Chapter', 'Review 3NF, BCNF, and 4NF with examples', 2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'COMPLETED', 60),
    (3, 5, 'Process Scheduling Sim', 'Implement Round Robin and FCFS in C', 1, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 'IN_PROGRESS', 150),
    (4, 8, 'SRS Document Submission', 'Draft IEEE standard SRS for semester project', 1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'COMPLETED', 240);

-- Insert sample exams
INSERT INTO exams (subject_id, exam_name, exam_date, target_marks, marks_obtained) VALUES
    (1, 'CAT-1 DBMS', DATE_ADD(CURDATE(), INTERVAL 10 DAY), 50.0, -1.0),
    (2, 'CAT-1 DAA', DATE_ADD(CURDATE(), INTERVAL 12 DAY), 50.0, -1.0),
    (3, 'CAT-1 Java', DATE_SUB(CURDATE(), INTERVAL 5 DAY), 50.0, 42.0),
    (4, 'CAT-1 CN', DATE_ADD(CURDATE(), INTERVAL 3 DAY), 50.0, -1.0);

-- Insert sample assignments
INSERT INTO assignments (subject_id, title, deadline, status, marks) VALUES
    (1, 'Normalization Assignment', DATE_ADD(CURDATE(), INTERVAL 4 DAY), 'PENDING', -1.0),
    (3, 'JavaFX GUI Assignment', DATE_ADD(CURDATE(), INTERVAL 6 DAY), 'IN_PROGRESS', -1.0);

-- Insert sample study sessions
INSERT INTO study_sessions (student_id, subject_id, topic, duration_minutes, session_date) VALUES
    (1, 1, 'SQL Joins and Subqueries', 90, CURDATE()),
    (1, 2, 'Greedy Algorithms', 60, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
    (1, 3, 'JavaFX Layouts', 120, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
    (1, 3, 'JDBC and DAO Pattern', 75, CURDATE()),
    (1, 1, 'Relational Algebra', 45, DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
    (1, 4, 'OSI Model and TCP/IP', 60, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
    (3, 5, 'CPU Scheduling Algorithms', 90, CURDATE()),
    (4, 8, 'Agile & Scrum Methodology', 110, DATE_SUB(CURDATE(), INTERVAL 1 DAY));


