# ⚡ SmartStudy: Intelligent Academic Planning & Progress Management System

> **A Comprehensive Dual-Tier Academic Planning Platform Built with Core Java, JavaFX, JDBC, MySQL, Jakarta Servlets & JSP.**  
> Designed for university academic environments to streamline student coursework tracking, exam preparation, and study analytics, while providing faculty members with a centralized web portal for cohort-wide performance monitoring.

---

## 📑 Table of Contents
1. [Executive Summary](#1-executive-summary)
2. [System Architecture](#2-system-architecture)
3. [Key Modules & Functionality](#3-key-modules--functionality)
4. [Implementation of Core Java Concepts](#4-implementation-of-core-java-concepts)
5. [Database Architecture & Relational Schema](#5-database-architecture--relational-schema)
6. [Project Directory Structure](#6-project-directory-structure)
7. [Installation & Execution Guide](#7-installation--execution-guide)
8. [Demonstration Credentials](#8-demonstration-credentials)

---

## 1. Executive Summary

In higher education, students frequently manage complex schedules spanning multiple credit-heavy courses, overlapping project deadlines, and approaching midterm evaluations. Traditional to-do utilities offer static task lists without contextual awareness of academic deadlines or course weightings.

**SmartStudy** addresses this by delivering an end-to-end, data-driven academic productivity system with two distinct user interfaces operating over a shared persistence and business logic core:
- **Student Desktop Interface (JavaFX)**: A reactive, local desktop application providing focused study session tracking, priority-ordered task queues, automated recommendations, and non-blocking background weekly academic report generation.
- **Faculty / Administrator Web Portal (Jakarta Servlets & JSP)**: A web-based management portal running on Apache Tomcat enabling instructors to inspect cohort-wide progress statistics, view individual student course breakdown and coursework status, and examine generated academic reports.

---

## 2. System Architecture

The application adopts a modular multi-tier architecture adhering to the **Separation of Concerns (SoC)** principle:

```text
+-----------------------------------------------------------------------------------+
|                                 PRESENTATION TIER                                 |
|                                                                                   |
|   [Student Desktop Client]                              [Faculty Web Portal]      |
|    - JavaFX 21 (MVC with FXML)                           - Jakarta Servlets 6.0   |
|    - Modern Dark Glassmorphic CSS                        - Jakarta JSP 3.1 & CSS  |
|    - Responsive Stage & Root Switching                   - Apache Tomcat 11.0     |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
|                                  BUSINESS TIER                                    |
|                                                                                   |
|   - Service Layer: UserService, SubjectService, TaskService, ProgressService      |
|   - Strategy Design Pattern: Runtime priority scoring (Deadline vs. Exam)         |
|   - Java Collections API: PriorityQueue, HashMap, HashSet, ArrayList              |
|   - Concurrency: Background ThreadManager (ExecutorService & JavaFX Tasks)        |
|   - File I/O: ReportGenerator template pattern (BufferedReader / BufferedWriter)   |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
|                                PERSISTENCE TIER                                   |
|                                                                                   |
|   - DAO Layer: UserDAO, SubjectDAO, TaskDAO, ExamDAO, StudySessionDAO             |
|   - Parameterized PreparedStatement queries (SQL Injection Prevention)            |
|   - JDBC Connection Factory (DBConnection with connection pooling logic)          |
|   - Relational Database: MySQL 8.0+ (InnoDB, Foreign Keys, Cascade Constraints)   |
+-----------------------------------------------------------------------------------+
```

---

## 3. Key Modules & Functionality

### 3.1 Student Desktop Application (JavaFX)
- **Academic Dashboard**: High-level visual KPI summary showing overall degree progress, total recorded study hours, pending assignments, and upcoming exam count.
- **Rule-Based Recommendation Engine**: Real-time analytical rules evaluating upcoming exams against current subject progress to generate dynamic warnings (e.g., flagging courses with imminent exams and low completion rates).
- **Urgent Priority Task Queue**: Prioritizes academic tasks using a `PriorityQueue` integrated with dynamic scoring algorithms.
- **Course & Subject Management**: Full CRUD interface for maintaining course codes, credits, and target grades.
- **Task & Coursework Tracker**: To-do management with status filtering (`ALL`, `PENDING`, `IN_PROGRESS`, `COMPLETED`), deadline validation, and completion logging.
- **Exam Scheduler & Marks Tracker**: Tracks scheduled midterms/finals, target scores, and recorded evaluation results.
- **Study Session Timer & Pomodoro**: Focus time tracker with one-click presets (25m Pomodoro, 60m, 90m) and topic-based logging.
- **Progress Analytics**: Visual completion indicators per subject and tracking of unique academic topics mastered.
- **Asynchronous Weekly Report Generator**: Background multithreaded worker that compiles academic metrics and exports formatted TXT reports without interrupting user interaction.

### 3.2 Faculty Web Portal (Jakarta Servlets & JSP)
- **Cohort Performance Dashboard**: Aggregates live academic metrics across all enrolled students directly from MySQL (Cohort Average Progress, Total Study Hours Logged, Active vs. Completed Tasks).
- **Student Roster**: Directory of enrolled students displaying university roll numbers, names, and institutional emails.
- **Detailed Student Academic Profile**: In-depth inspection view for any selected student displaying their enrolled courses, credit load, target grades, study hours, and active coursework table.
- **Server Report Browser**: File explorer allowing faculty to inspect generated weekly academic TXT reports stored on disk.

---

## 4. Implementation of Core Java Concepts

Every major object-oriented and enterprise Java paradigm is integrated into the system:

### 4.1 Abstraction
- **Abstract User Entity**: [`com.smartstudy.model.User`](src/main/java/com/smartstudy/model/User.java) encapsulates shared user identity attributes (`id`, `name`, `email`, `password`, `role`) and defines the contract method `public abstract String getDashboardType();`. It prevents direct instantiation of generic user objects.
- **Abstract Report Generator**: [`com.smartstudy.report.ReportGenerator`](src/main/java/com/smartstudy/report/ReportGenerator.java) defines a template method for constructing header, body, and footer sections of academic reports, leaving concrete formatting to subclasses.

### 4.2 Inheritance
- **Role Hierarchy**: [`Student`](src/main/java/com/smartstudy/model/Student.java) and [`Teacher`](src/main/java/com/smartstudy/model/Teacher.java) extend [`User`](src/main/java/com/smartstudy/model/User.java), inheriting authentication and identification fields while introducing role-specific fields (e.g., `Student.rollNumber`) and role-specific implementations of abstract methods.
- **Report Hierarchy**: [`StudentReportGenerator`](src/main/java/com/smartstudy/report/StudentReportGenerator.java) extends [`ReportGenerator`](src/main/java/com/smartstudy/report/ReportGenerator.java), implementing the specific multi-section text layout for weekly student progress.

### 4.3 Polymorphism & The Strategy Design Pattern
- **Priority Calculation Strategies**: Implemented via [`PriorityStrategy`](src/main/java/com/smartstudy/strategy/PriorityStrategy.java):
  - [`DeadlinePriorityStrategy`](src/main/java/com/smartstudy/strategy/DeadlinePriorityStrategy.java): Calculates priority based strictly on days remaining before the assignment deadline.
  - [`ExamPriorityStrategy`](src/main/java/com/smartstudy/strategy/ExamPriorityStrategy.java): Boosts task priority if an exam for that course is scheduled within a 7-day window.
- **Runtime Polymorphism**: The [`RecommendationService`](src/main/java/com/smartstudy/service/RecommendationService.java) switches between strategy implementations at runtime depending on the student's upcoming exam schedule, demonstrating behavioral polymorphism and the Open/Closed Principle.

### 4.4 User-Defined Exceptions
- [`InvalidTaskException`](src/main/java/com/smartstudy/exception/InvalidTaskException.java): Checked exception thrown by [`TaskService`](src/main/java/com/smartstudy/service/TaskService.java) when task input violates business rules (e.g., empty title, past deadline).
- [`InvalidLoginException`](src/main/java/com/smartstudy/exception/InvalidLoginException.java): Checked exception thrown by [`UserService`](src/main/java/com/smartstudy/service/UserService.java) when user credentials fail database verification or input parameters are malformed.

### 4.5 Java Collections Framework
- **`PriorityQueue<Task>`** ([`TaskPriorityQueue.java`](src/main/java/com/smartstudy/collections/TaskPriorityQueue.java)): Backed by a min-heap comparator to ensure the most urgent academic tasks are ordered first ($O(\log n)$ insertion, $O(1)$ retrieval).
- **`HashMap<Integer, Subject>`** ([`SubjectService.java`](src/main/java/com/smartstudy/service/SubjectService.java)): In-memory key-value cache mapping subject IDs to subject models for constant-time ($O(1)$) lookups during task rendering.
- **`HashSet<String>`** ([`ProgressService.java`](src/main/java/com/smartstudy/service/ProgressService.java)): Collects topics from study session logs and automatically deduplicates entries to calculate the exact count of unique academic topics studied.
- **`ArrayList<Task>`** ([`TaskService.java`](src/main/java/com/smartstudy/service/TaskService.java)): Dynamic list structure utilized for sorting, predicate filtering, and populating JavaFX `ObservableList` collections.

### 4.6 Multithreading & Concurrency
- **Non-Blocking UI Execution**: In [`DashboardController.java`](src/main/java/com/smartstudy/javafx/controller/DashboardController.java), report generation is decoupled from the JavaFX Application Thread using `javafx.concurrent.Task<String>`.
- **Thread Management**: [`ThreadManager.java`](src/main/java/com/smartstudy/javafx/ThreadManager.java) manages a thread pool via `ExecutorService` configured with daemon worker threads. The UI remains fully responsive, updating a progress bar while background threads compute metrics and perform file writes.

### 4.7 Java File I/O
- **Report Persistence**: [`FileUtil.java`](src/main/java/com/smartstudy/util/FileUtil.java) implements `BufferedWriter` wrapping a `FileWriter` inside try-with-resources blocks to write formatted text reports to disk.
- **Report Parsing**: Uses `BufferedReader` wrapping a `FileReader` with line-by-line reading into `StringBuilder` to load saved reports for preview in both the desktop app and the web portal.

### 4.8 JDBC & Data Access Object (DAO) Pattern
- **Separation of Concerns**: Database operations are abstracted behind clean DAO interfaces ([`UserDAO`](src/main/java/com/smartstudy/dao/UserDAO.java), [`SubjectDAO`](src/main/java/com/smartstudy/dao/SubjectDAO.java), [`TaskDAO`](src/main/java/com/smartstudy/dao/TaskDAO.java), [`ExamDAO`](src/main/java/com/smartstudy/dao/ExamDAO.java), [`StudySessionDAO`](src/main/java/com/smartstudy/dao/StudySessionDAO.java)).
- **SQL Injection Prevention**: All DAO implementations exclusively employ `java.sql.PreparedStatement` with parameterized placeholders (`?`).
- **Connection Management**: Centralized in [`DBConnection.java`](src/main/java/com/smartstudy/util/DBConnection.java) using JDBC driver `com.mysql.cj.jdbc.Driver`.

### 4.9 Jakarta Servlets 6.0 & JSP 3.1
- **Servlet Controllers**: Annotated with `@WebServlet` ([`LoginServlet`](src/main/java/com/smartstudy/servlet/LoginServlet.java), [`DashboardServlet`](src/main/java/com/smartstudy/servlet/DashboardServlet.java), [`StudentServlet`](src/main/java/com/smartstudy/servlet/StudentServlet.java), [`ProgressServlet`](src/main/java/com/smartstudy/servlet/ProgressServlet.java), [`ReportServlet`](src/main/java/com/smartstudy/servlet/ReportServlet.java)) to process HTTP GET/POST requests and forward data to views.
- **Session Tracking**: Managed via `jakarta.servlet.http.HttpSession` to preserve teacher identity across requests and handle authentication invalidation upon logout.
- **View Templates**: JSP pages in `src/main/webapp/` render dynamic HTML using scriptlets, expressions, and scoped attributes.

---

## 5. Database Architecture & Relational Schema

The database schema is defined in [`database/schema.sql`](database/schema.sql). It consists of 6 tables with foreign key constraints and cascade actions:

```text
    +-------------------+           +-----------------------+
    |       users       |           |       subjects        |
    +-------------------+           +-----------------------+
    | id (PK)           |<-----+    | id (PK)               |<-----+
    | name              |      |    | student_id (FK->users)|      |
    | email (UNIQUE)    |      |    | name                  |      |
    | password          |      |    | course_code           |      |
    | role (ENUM)       |      |    | credits               |      |
    | created_at        |      |    | target_marks          |      |
    +-------------------+      |    +-----------------------+      |
                               |                |                  |
           +-------------------+                +--------+         |
           |                                             |         |
           v                                             v         |
+--------------------------+                 +---------------------+
|          tasks           |                 |        exams        |
+--------------------------+                 +---------------------+
| id (PK)                  |                 | id (PK)             |
| student_id (FK->users)   |                 | subject_id (FK->sub)|
| subject_id (FK->subjects)|                 | exam_name           |
| title                    |                 | exam_date           |
| description              |                 | target_marks        |
| priority (INT)           |                 | marks_obtained      |
| deadline (DATE)          |                 +---------------------+
| status (ENUM)            |
| estimated_minutes        |                 +---------------------+
+--------------------------+                 |     assignments     |
                                             +---------------------+
+--------------------------+                 | id (PK)             |
|      study_sessions      |                 | subject_id (FK->sub)|
+--------------------------+                 | title               |
| id (PK)                  |                 | deadline (DATE)     |
| student_id (FK->users)   |                 | status (ENUM)       |
| subject_id (FK->subjects)|                 | marks               |
| topic                    |                 +---------------------+
| duration_minutes (INT)   |
| session_date (DATE)      |
+--------------------------+
```

---

## 6. Project Directory Structure

```text
SmartStudy/
├── pom.xml                                  <- Maven Project Object Model (Dependencies & Build Specs)
├── run.bat                                  <- Windows CMD launcher script
├── run.ps1                                  <- Windows PowerShell launcher script
├── README.md                                <- Master technical documentation
├── database/
│   └── schema.sql                           <- MySQL DDL schema and seed data
│
├── src/main/java/com/smartstudy/
│   ├── Launcher.java                        <- Standalone bootstrap entry point for JavaFX
│   ├── Main.java                            <- JavaFX Application class managing stage and root-swapping
│   │
│   ├── model/                               <- Domain entities (Abstraction, Inheritance, Encapsulation)
│   │   ├── User.java                        <- Abstract base user class
│   │   ├── Student.java                     <- Concrete student subclass (rollNumber, overrides)
│   │   ├── Teacher.java                     <- Concrete faculty subclass
│   │   ├── Subject.java                     <- Course entity (code, credits, target marks)
│   │   ├── Task.java                        <- Task entity implementing Comparable
│   │   ├── Exam.java                        <- Exam scheduling entity
│   │   ├── Assignment.java                  <- Course assignment entity
│   │   └── StudySession.java                <- Logged study duration entity
│   │
│   ├── strategy/                            <- Strategy Design Pattern
│   │   ├── PriorityStrategy.java            <- Strategy contract interface
│   │   ├── DeadlinePriorityStrategy.java    <- Proximity-to-deadline scoring algorithm
│   │   └── ExamPriorityStrategy.java        <- Upcoming-exam priority boost algorithm
│   │
│   ├── collections/                         <- Custom Collections Wrappers
│   │   └── TaskPriorityQueue.java           <- PriorityQueue wrapper for prioritized task retrieval
│   │
│   ├── exception/                           <- Custom Checked Exceptions
│   │   ├── InvalidTaskException.java        <- Thrown on invalid task deadlines/parameters
│   │   └── InvalidLoginException.java       <- Thrown on authentication failure
│   │
│   ├── util/                                <- Utilities & Persistence Helpers
│   │   ├── DBConnection.java                <- JDBC connection factory for MySQL
│   │   ├── FileUtil.java                    <- Java File I/O helper (BufferedReader / BufferedWriter)
│   │   ├── ValidationUtil.java              <- Regular expression and boundary validators
│   │   └── UserSession.java                 <- Singleton tracking active authenticated session
│   │
│   ├── dao/                                 <- Data Access Object (DAO) Interfaces
│   │   ├── UserDAO.java
│   │   ├── SubjectDAO.java
│   │   ├── TaskDAO.java
│   │   ├── ExamDAO.java
│   │   └── StudySessionDAO.java
│   │
│   ├── dao/impl/                            <- JDBC PreparedStatement DAO Implementations
│   │   ├── UserDAOImpl.java
│   │   ├── SubjectDAOImpl.java
│   │   ├── TaskDAOImpl.java
│   │   ├── ExamDAOImpl.java
│   │   └── StudySessionDAOImpl.java
│   │
│   ├── service/                             <- Business Logic & Application Services
│   │   ├── UserService.java                 <- Authentication and role filtering
│   │   ├── SubjectService.java              <- Subject operations with HashMap cache
│   │   ├── TaskService.java                 <- Task business rules, validation, ArrayList filtering
│   │   ├── ProgressService.java             <- Progress analytics, study hours, HashSet deduplication
│   │   └── RecommendationService.java       <- Rule engine coordinating priority strategies
│   │
│   ├── report/                              <- Academic Report Generation Subsystem
│   │   ├── ReportGenerator.java             <- Abstract template defining report structure
│   │   └── StudentReportGenerator.java      <- Concrete report formatter outputting weekly TXT summaries
│   │
│   ├── javafx/                              <- JavaFX Desktop Layer
│   │   ├── ThreadManager.java               <- ExecutorService managing background worker threads
│   │   └── controller/                      <- JavaFX MVC Screen Controllers
│   │       ├── LoginController.java
│   │       ├── DashboardController.java
│   │       ├── SubjectController.java
│   │       ├── TaskController.java
│   │       ├── ExamController.java
│   │       ├── StudyController.java
│   │       └── ProgressController.java
│   │
│   └── servlet/                             <- Jakarta Servlets for Faculty Web Portal
│       ├── LoginServlet.java                <- Web authentication controller (/web-login)
│       ├── DashboardServlet.java            <- Live cohort analytics aggregator (/web-dashboard)
│       ├── StudentServlet.java              <- Enrolled student roster controller (/students)
│       ├── ProgressServlet.java             <- Individual student academic profile (/student-progress)
│       └── ReportServlet.java               <- Server-side report explorer (/web-reports)
│
├── src/main/resources/
│   ├── fxml/                                <- JavaFX FXML Layout Templates
│   │   ├── login.fxml
│   │   ├── dashboard.fxml
│   │   ├── subjects.fxml
│   │   ├── tasks.fxml
│   │   ├── exams.fxml
│   │   ├── study.fxml
│   │   └── progress.fxml
│   └── css/
│       └── style.css                        <- Desktop theme stylesheet
│
└── src/main/webapp/                         <- Web Application Layer (Tomcat deployment)
    ├── WEB-INF/
    │   └── web.xml                          <- Deployment descriptor (Servlet 6.0 specification)
    ├── login.jsp                            <- Faculty login interface
    ├── dashboard.jsp                        <- Cohort performance analytics view
    ├── students.jsp                         <- Enrolled student directory view
    ├── student-progress.jsp                 <- Selected student coursework view
    ├── reports.jsp                          <- Academic report preview view
    └── css/
        └── web-style.css                    <- Web portal stylesheet
```

---

## 7. Installation & Execution Guide

### 7.1 Prerequisites
- **Java Development Kit (JDK) 21** or higher.
- **XAMPP / MySQL Server** (running on default port `3306`).
- **Eclipse IDE for Enterprise Java and Web Developers** (or Apache Tomcat 10/11 standalone).

---

### 7.2 Database Setup
1. Launch **XAMPP Control Panel** and start **MySQL**.
2. Execute the schema script using MySQL CLI or phpMyAdmin:
   ```bash
   "C:\xampp\mysql\bin\mysql.exe" -u root < database/schema.sql
   ```
   *(Creates the `smartstudy` database, establishes 6 tables, and populates sample students, courses, tasks, and study records).*

---

### 7.3 Executing the JavaFX Desktop Client

#### Option A: Running from Eclipse IDE
1. Open Eclipse -> **File** -> **Import...** -> **Maven** -> **Existing Maven Projects**.
2. Select the `SmartStudy` directory containing `pom.xml`.
3. In Project Explorer, locate [`src/main/java/com/smartstudy/Launcher.java`](src/main/java/com/smartstudy/Launcher.java).
4. Right-click [`Launcher.java`](src/main/java/com/smartstudy/Launcher.java) -> **Run As** -> **Java Application**.
   *(Note: Executing `Launcher.java` resolves Java 11+ modular boot layer restrictions without requiring VM argument flags).*

#### Option B: Running from Terminal / PowerShell
```powershell
.\run.ps1
```
*(Or in Windows Command Prompt: `.\run.bat`)*

---

### 7.4 Executing the Faculty Web Portal (Tomcat)

1. In Eclipse, right-click the `SmartStudy` project -> **Run As** -> **Run on Server**.
2. Select **Apache Tomcat v11.0** (or Tomcat 10) and click **Finish**.
3. Access the web portal in any browser:
   ```
   http://localhost:8080/SmartStudy/login.jsp
   ```

---

## 8. Demonstration Credentials

Pre-seeded database accounts available for testing:

| Interface | Role | Account Name | Email | Password |
| :--- | :--- | :--- | :--- | :--- |
| **JavaFX Desktop** | Student | Alex Johnson | `alex@smartstudy.edu` | `pass123` |
| **JavaFX Desktop** | Student | Rohan Patel | `rohan@smartstudy.edu` | `pass123` |
| **JavaFX Desktop** | Student | Sneha Reddy | `sneha@smartstudy.edu` | `pass123` |
| **Web Portal** | Faculty | Prof. Sharma | `sharma@smartstudy.edu` | `pass123` |
