# ⚡ SmartStudy: Intelligent Academic Planning & Progress Management System

> **A Comprehensive Academic Management Platform Built with Java, JavaFX, JDBC, MySQL, Servlets & JSP.**  
> Designed for university students to track coursework, assignments, exams, and study hours with intelligent rule-based study recommendations, non-blocking background report generation, and dual-interface architecture (Desktop App + Web Portal).

---

## 📑 Table of Contents
1. [Architectural Overview & System Design](#1-architectural-overview--system-design)
2. [Java Concepts & Technical Mapping (Viva Ready)](#2-java-concepts--technical-mapping-viva-ready)
3. [Step-by-Step Live Demo Script for Evaluators](#3-step-by-step-live-demo-script-for-evaluators)
4. [Complete Project Structure & File Guide](#4-complete-project-structure--file-guide)
5. [Database Schema & Seed Data (MySQL via XAMPP)](#5-database-schema--seed-data-mysql-via-xampp)
6. [How to Run the Project (Eclipse IDE & Terminal)](#6-how-to-run-the-project-eclipse-ide--terminal)
7. [Teacher Portal: JavaFX vs. JSP/Servlet Architecture](#7-teacher-portal-javafx-vs-jspservlet-architecture)
8. [3-Person Team Division & Presentation Guide](#8-3-person-team-division--presentation-guide)

---

## 1. Architectural Overview & System Design

```text
                          +------------------------------------------------+
                          |                   SMARTSTUDY                   |
                          +-----------------------+------------------------+
                                                  |
                  +-------------------------------+-------------------------------+
                  |                                                               |
     [Desktop Client: JavaFX]                                       [Web Portal: JSP + Servlets]
  Main student application with FXML,                           Teacher & administrator monitoring portal
  CSS, reactive GUI & multithreading                            for batch analytics and student progress
                  |                                                               |
                  +-------------------------------+-------------------------------+
                                                  |
                                       +--------------------+
                                       |   Service Layer    |
                                       +--------------------+
                                       |  Strategy Pattern  |
                                       |  Collections API   |
                                       |  Validation & Rule |
                                       |  Recommendation    |
                                       +---------+----------+
                                                 |
                                       +--------------------+
                                       |     DAO Layer      |
                                       +--------------------+
                                       | UserDAO, TaskDAO   |
                                       | ExamDAO, SubjectDAO|
                                       | StudySessionDAO    |
                                       +---------+----------+
                                                 |
                                       +--------------------+
                                       |   JDBC / MySQL     |
                                       |  (XAMPP Port 3306) |
                                       +--------------------+
```

- **Dual Frontends**:
  - **Student Desktop Interface (JavaFX)**: Main daily hub for students to manage subjects, create tasks, log study timers, view analytics, and generate weekly TXT reports in the background.
  - **Teacher / Admin Web Portal (JSP + Servlets)**: Web portal accessible via web browser for faculty to view student rosters, inspect individual course progress, and review generated reports.
- **Shared Backend**: Both clients share the exact same Service, Strategy, DAO, and MySQL database layer.
- **End-to-End Real Database Integration**: Fully connected to MySQL via JDBC (`com.mysql.cj.jdbc.Driver`). Data added via the desktop client or web portal is persisted directly in MySQL.

---

## 2. Java Concepts & Technical Mapping (Viva Ready)

Every core Java curriculum requirement is cleanly implemented and verifiable in source code:

| Java Concept | Exact Implementation | Source File Location |
| :--- | :--- | :--- |
| **Abstraction** | `User` declared `abstract` with abstract method `getDashboardType()`. `ReportGenerator` declared `abstract` with `generateReport()`. | [`User.java`](src/main/java/com/smartstudy/model/User.java)<br>[`ReportGenerator.java`](src/main/java/com/smartstudy/report/ReportGenerator.java) |
| **Inheritance** | `Student extends User` and `Teacher extends User` inheriting common user attributes (`id`, `name`, `email`, `password`, `role`). | [`Student.java`](src/main/java/com/smartstudy/model/Student.java)<br>[`Teacher.java`](src/main/java/com/smartstudy/model/Teacher.java) |
| **Polymorphism & Strategy Pattern** | `PriorityStrategy` interface/abstract class implemented by `DeadlinePriorityStrategy` and `ExamPriorityStrategy`. `RecommendationService` switches strategies at runtime to calculate task priority. | [`PriorityStrategy.java`](src/main/java/com/smartstudy/strategy/PriorityStrategy.java)<br>[`DeadlinePriorityStrategy.java`](src/main/java/com/smartstudy/strategy/DeadlinePriorityStrategy.java)<br>[`ExamPriorityStrategy.java`](src/main/java/com/smartstudy/strategy/ExamPriorityStrategy.java) |
| **User-Defined Exceptions** | `InvalidTaskException` thrown when task deadline is in the past or title is missing.<br>`InvalidLoginException` thrown on bad credentials. | [`InvalidTaskException.java`](src/main/java/com/smartstudy/exception/InvalidTaskException.java)<br>[`InvalidLoginException.java`](src/main/java/com/smartstudy/exception/InvalidLoginException.java) |
| **Java Collections Framework** | • **`PriorityQueue<Task>`**: Sorts urgent tasks by computed priority score.<br>• **`HashMap<Integer, Subject>`**: $O(1)$ fast subject lookup by ID.<br>• **`HashSet<String>`**: Deduplicates completed academic topics.<br>• **`ArrayList<Task>`**: Dynamic filtering of pending and completed tasks. | [`TaskPriorityQueue.java`](src/main/java/com/smartstudy/collections/TaskPriorityQueue.java)<br>[`SubjectService.java`](src/main/java/com/smartstudy/service/SubjectService.java)<br>[`ProgressService.java`](src/main/java/com/smartstudy/service/ProgressService.java)<br>[`TaskService.java`](src/main/java/com/smartstudy/service/TaskService.java) |
| **Multithreading** | Asynchronous weekly report generation using JavaFX `Task<String>` executed on a daemon background `ExecutorService`, keeping the JavaFX UI 100% responsive. | [`ThreadManager.java`](src/main/java/com/smartstudy/javafx/ThreadManager.java)<br>[`DashboardController.java`](src/main/java/com/smartstudy/javafx/controller/DashboardController.java) |
| **Java File I/O** | `BufferedWriter` / `BufferedReader` writing timestamped weekly TXT reports to disk and reading them back for in-app preview. | [`FileUtil.java`](src/main/java/com/smartstudy/util/FileUtil.java)<br>[`StudentReportGenerator.java`](src/main/java/com/smartstudy/report/StudentReportGenerator.java) |
| **JDBC Persistence** | PreparedStatement parameterized queries, connection pooling logic, and full CRUD separation with the DAO design pattern. | [`DBConnection.java`](src/main/java/com/smartstudy/util/DBConnection.java)<br>`com.smartstudy.dao.impl.*` |
| **JavaFX Desktop GUI** | Clean MVC architecture with FXML layouts, controller binding, observable collections, and custom modern dark theme. | [`src/main/resources/fxml/`](src/main/resources/fxml/)<br>[`style.css`](src/main/resources/css/style.css) |
| **Servlets & JSP** | Jakarta Servlet 6.0 request routing (`@WebServlet`) and Jakarta JSP 3.1 templates rendering dynamic data with JSTL. | [`src/main/java/com/smartstudy/servlet/`](src/main/java/com/smartstudy/servlet/)<br>[`src/main/webapp/`](src/main/webapp/) |

---

## 3. Step-by-Step Live Demo Script for Evaluators

When presenting this project to your professor or examiner, follow this 7-step sequence:

### Step 1: Launch & Demonstrate Authentication & OOP Hierarchy
1. Run `.\run.bat` (or in PowerShell `.\run.ps1`, or Run in Eclipse).
2. Show the **Sign In Screen**.
3. **What to explain**:
   > *"We have an abstract parent class `User` extended by `Student` and `Teacher`. Authentication is handled in `UserService.java` querying MySQL via `UserDAO`. If invalid credentials are provided, our custom exception `InvalidLoginException` is caught and displayed."*
4. Click the convenient **"Demo Student Login"** button (`alex@smartstudy.edu` / `pass123`).

### Step 2: Dashboard, KPI Cards & Intelligent Recommendations
1. You land on the **Dashboard**:
   - Notice the **4 KPI cards**: Overall Academic Progress (%), Total Study Hours, Pending Tasks, and Upcoming Exams — all queried live from MySQL!
   - Look at the **💡 Intelligent Rule-Based Recommendations** section:
     - The engine checks upcoming exams (e.g. DBMS exam in 10 days) against current progress to dynamically flag urgent areas!
   - Look at the **🔥 Urgent Task Queue**:
     - Uses **Java Collections `PriorityQueue`** combined with the **Strategy Pattern** to prioritize tasks by deadline proximity and weight.
2. **What to explain**:
   > *"The dashboard demonstrates our rule-based intelligence engine implemented in `RecommendationService.java` using the Strategy Pattern to dynamically recalculate priorities based on deadlines and exams."*

### Step 3: Demonstrate Multithreading & File I/O
1. On the dashboard, locate the **"Weekly Academic Report Generator"** card.
2. Click **"⚡ Generate Report (Background Thread)"**.
3. Observe:
   - The UI does **not freeze** at all.
   - The animated progress bar advances through stages (*Fetching data -> Calculating progress -> Writing to disk*).
   - Once completed, the full formatted TXT report appears immediately in the preview box!
4. Click **"📂 View Previous Report"**:
   - Demonstrates `FileUtil.java` reading the saved file back from the `reports/` folder.
5. **What to explain**:
   > *"This highlights Java multithreading via `ThreadManager.java` using `javafx.concurrent.Task` and an `ExecutorService`. The file is written to disk using Java File I/O in `FileUtil.java` using `BufferedWriter` and `BufferedReader`."*

### Step 4: Subject Management & Collections HashMap
1. In the sidebar, click **"📚 Subjects"**.
2. Point out the enrolled courses from MySQL (DBMS, DAA, Java, CN).
3. Fill out the form to add a new subject (e.g., `Cloud Computing`, code `CSE3005`, credits `3`, target `85%`) and click **"Add Subject"**.
4. The subject is inserted into MySQL, and the table instantly refreshes via JavaFX `ObservableList`.
5. **What to explain**:
   > *"In `SubjectService.java`, we maintain an in-memory `HashMap<Integer, Subject>` providing instant $O(1)$ lookup by subject ID alongside MySQL persistence."*

### Step 5: Task Management & Testing Custom Exception (`InvalidTaskException`)
1. Click **"📝 Tasks & To-Dos"**.
2. Notice the table of tasks, filtering by status (*All, Pending, Completed*), and **"✔ Mark Complete"**.
3. **Highlight the custom exception test**:
   - Click the dedicated button: **"⚠️ Test Invalid Deadline (InvalidTaskException)"**.
   - Notice the red alert: `Verified Custom Exception: [InvalidTaskException] Task deadline cannot be in the past`.
   - Try adding a task with an empty title; `TaskService` will reject it with `InvalidTaskException`.
4. **What to explain**:
   > *"This satisfies the user-defined exception requirement. In `TaskService.java`, adding any task with a past deadline or missing title throws our custom checked exception `InvalidTaskException`, which the UI catches and displays gracefully."*

### Step 6: Exams, Marks & Study Session Tracker
1. Click **"🎯 Exams & Marks"**:
   - Demonstrates scheduling exams, setting target marks, and recording actual marks obtained once the exam is evaluated (persisted to MySQL).
2. Click **"⏱ Study Tracker"**:
   - Displays total study hours logged from MySQL.
   - Students can log focused study blocks with one-click presets (**25m Pomodoro**, **60m**, **90m**).

### Step 7: Progress Analytics & HashSet Deduplication
1. Click **"📈 Progress Analytics"**.
2. View overall progress bar alongside subject-wise completion bars and target comparisons.
3. Notice the metric: **`X Unique Topics Studied`**.
4. **What to explain**:
   > *"In `ProgressService.java`, we pass student study logs through a `HashSet<String>` to eliminate duplicates and calculate the exact count of unique academic topics mastered."*

---

## 4. Complete Project Structure & File Guide

```text
SmartStudy/
│
├── pom.xml                                  <- Maven POM (Java 21, JavaFX 21, MySQL Connector 8.3, Jakarta Servlet 6.0 & JSP 3.1)
├── run.bat                                  <- One-click Windows CMD launcher
├── run.ps1                                  <- One-click PowerShell launcher
├── README.md                                <- Complete documentation and viva guide
│
├── database/
│   └── schema.sql                           <- MySQL database schema (6 tables) with multi-student seed data
│
├── src/main/java/com/smartstudy/
│   ├── Launcher.java                        <- Main entry point avoiding JavaFX module classpath check
│   ├── Main.java                            <- JavaFX Application class managing stage and scene transitions
│   │
│   ├── model/                               <- Core Domain Models (OOP: Abstraction & Inheritance)
│   │   ├── User.java                        <- Abstract parent class (id, name, email, password, role)
│   │   ├── Student.java                     <- Concrete subclass representing students (with rollNumber)
│   │   ├── Teacher.java                     <- Concrete subclass representing faculty
│   │   ├── Subject.java                     <- Course model (code, credits, targetMarks)
│   │   ├── Task.java                        <- Academic task/assignment (deadline, priority, status)
│   │   ├── Exam.java                        <- Exam model (examDate, targetMarks, marksObtained)
│   │   ├── Assignment.java                  <- Assignment model (deadline, status, marks)
│   │   └── StudySession.java                <- Study log model (topic, durationMinutes, sessionDate)
│   │
│   ├── strategy/                            <- Strategy Design Pattern (Polymorphism)
│   │   ├── PriorityStrategy.java            <- Abstract Strategy base defining calculatePriority(Task)
│   │   ├── DeadlinePriorityStrategy.java    <- Priority calculation based on days remaining until deadline
│   │   └── ExamPriorityStrategy.java        <- Priority boost for tasks associated with upcoming exams
│   │
│   ├── collections/                         <- Custom Collections Wrappers
│   │   └── TaskPriorityQueue.java           <- PriorityQueue wrapper sorting tasks by computed priority
│   │
│   ├── exception/                           <- Custom User-Defined Exceptions
│   │   ├── InvalidTaskException.java        <- Thrown for past deadlines or invalid task parameters
│   │   └── InvalidLoginException.java       <- Thrown for invalid authentication credentials
│   │
│   ├── util/                                <- Utilities & Helpers
│   │   ├── DBConnection.java                <- JDBC connection factory for MySQL with auto-close
│   │   ├── ValidationUtil.java              <- Input validators for dates, marks, emails, and strings
│   │   ├── FileUtil.java                    <- Java File I/O helper (BufferedReader / BufferedWriter)
│   │   └── UserSession.java                 <- Singleton session state tracking authenticated user
│   │
│   ├── dao/                                 <- DAO Interfaces (Separation of Concerns)
│   │   ├── UserDAO.java                     <- Includes authenticate, findById, findByEmail, findByRole
│   │   ├── SubjectDAO.java
│   │   ├── TaskDAO.java
│   │   ├── ExamDAO.java
│   │   └── StudySessionDAO.java
│   │
│   ├── dao/impl/                            <- DAO JDBC Implementations
│   │   ├── UserDAOImpl.java
│   │   ├── SubjectDAOImpl.java
│   │   ├── TaskDAOImpl.java
│   │   ├── ExamDAOImpl.java
│   │   └── StudySessionDAOImpl.java
│   │
│   ├── service/                             <- Business Logic Layer
│   │   ├── UserService.java                 <- Authentication and role-based user retrieval
│   │   ├── SubjectService.java              <- Subject management with HashMap caching
│   │   ├── TaskService.java                 <- Task CRUD, validation, and ArrayList filtering
│   │   ├── ProgressService.java             <- Progress calculation, study hours, HashSet topics
│   │   └── RecommendationService.java       <- Rule-based intelligence engine using Strategy Pattern
│   │
│   ├── report/                              <- Academic Report Generation
│   │   ├── ReportGenerator.java             <- Abstract class defining report generation structure
│   │   └── StudentReportGenerator.java      <- Concrete generator producing detailed weekly TXT report
│   │
│   ├── javafx/                              <- JavaFX Desktop Layer
│   │   ├── ThreadManager.java               <- Background thread manager (JavaFX Task + ExecutorService)
│   │   └── controller/
│   │       ├── LoginController.java         <- Authentication and role selection
│   │       ├── DashboardController.java     <- KPI metrics, recommendations, and background reports
│   │       ├── SubjectController.java       <- Course management and credits
│   │       ├── TaskController.java          <- Task table, filters, and InvalidTaskException demo
│   │       ├── ExamController.java          <- Exam scheduling and marks recording
│   │       ├── StudyController.java         <- Study logs and Pomodoro timer presets
│   │       └── ProgressController.java      <- Academic completion analytics and HashSet topics
│   │
│   └── servlet/                             <- Jakarta Servlets for Teacher Web Portal
│       ├── LoginServlet.java                <- Route: /web-login
│       ├── DashboardServlet.java            <- Route: /web-dashboard (cohort-wide metrics from MySQL)
│       ├── StudentServlet.java              <- Route: /students (queries all enrolled students from MySQL)
│       ├── ProgressServlet.java             <- Route: /student-progress (individual student progress from MySQL)
│       └── ReportServlet.java               <- Route: /web-reports (browse generated reports)
│
├── src/main/resources/                      <- JavaFX Presentation Assets
│   ├── fxml/
│   │   ├── login.fxml
│   │   ├── dashboard.fxml
│   │   ├── subjects.fxml
│   │   ├── tasks.fxml
│   │   ├── exams.fxml
│   │   ├── study.fxml
│   │   └── progress.fxml
│   └── css/
│       └── style.css                        <- Modern Dark Theme (Indigo/Slate glassmorphic palette)
│
└── src/main/webapp/                         <- Web Application Layer (JSP + Servlets)
    ├── WEB-INF/
    │   └── web.xml                          <- Deployment descriptor
    ├── login.jsp                            <- Faculty web login page
    ├── dashboard.jsp                        <- Faculty overview & live cohort metrics
    ├── students.jsp                         <- Enrolled student roster
    ├── student-progress.jsp                 <- Selected student's subject breakdown
    ├── reports.jsp                          <- Server-side generated report browser
    └── css/
        └── web-style.css                    <- Web portal styling
```

---

## 5. Database Schema & Seed Data (MySQL via XAMPP)

The database schema is located in [`database/schema.sql`](database/schema.sql).

### Tables:
1. `users` (id, name, email, password, role)
2. `subjects` (id, student_id, name, course_code, credits, target_marks)
3. `tasks` (id, student_id, subject_id, title, description, priority, deadline, status, estimated_minutes)
4. `exams` (id, subject_id, exam_name, exam_date, target_marks, marks_obtained)
5. `assignments` (id, subject_id, title, deadline, status, marks)
6. `study_sessions` (id, student_id, subject_id, topic, duration_minutes, session_date)

### Setup via XAMPP MySQL:
1. Open **XAMPP Control Panel** and click **Start** next to **MySQL** (and **Apache** if using phpMyAdmin).
2. The schema creates the database `smartstudy` automatically. To re-import or reset:
```bash
# Using XAMPP MySQL CLI:
"C:\xampp\mysql\bin\mysql.exe" -u root < database/schema.sql

# Or import database/schema.sql inside phpMyAdmin (http://localhost/phpmyadmin)
```

### Pre-loaded Seed Accounts:
| Role | Name | Email | Password | Details |
| :--- | :--- | :--- | :--- | :--- |
| **Student** | Alex Johnson | `alex@smartstudy.edu` | `pass123` | 4 subjects, 5 tasks, exams, study logs |
| **Teacher** | Prof. Sharma | `sharma@smartstudy.edu` | `pass123` | Faculty account for Web Portal |
| **Student** | Rohan Patel | `rohan@smartstudy.edu` | `pass123` | 3 subjects, Operating Systems task |
| **Student** | Sneha Reddy | `sneha@smartstudy.edu` | `pass123` | 3 subjects, Software Eng task |

---

## 6. How to Run the Project (Eclipse IDE & Terminal)

### Prerequisites
- **Java Development Kit (JDK) 21** or higher.
- **XAMPP** (with MySQL running on port 3306).
- **Eclipse IDE for Enterprise Java and Web Developers** (or Eclipse IDE for Java Developers).

---

### Method A: Running in Eclipse IDE

#### 1. Importing into Eclipse:
1. Open Eclipse -> **File** -> **Import...**
2. Choose **Maven** -> **Existing Maven Projects** -> Click **Next**.
3. Set **Root Directory** to `SmartStudy` folder (`C:\Users\Kapish\Desktop\vit projects\SmartStudy`).
4. Select `pom.xml` and click **Finish**.
5. Wait for Eclipse to resolve dependencies. If needed, right-click the project -> **Maven** -> **Update Project...** -> **OK**.

#### 2. Running the JavaFX Desktop Client in Eclipse:
1. In the Eclipse **Project Explorer**, navigate to:  
   `src/main/java` -> `com.smartstudy` -> [`Launcher.java`](src/main/java/com/smartstudy/Launcher.java).
2. Right-click [`Launcher.java`](src/main/java/com/smartstudy/Launcher.java) -> **Run As** -> **Java Application**.
   *(Note: Running `Launcher.java` avoids JavaFX module-path restrictions because it doesn't extend Application directly!)*
3. The JavaFX application window opens immediately!

#### 3. Running the Faculty Web Portal in Eclipse (Tomcat):
1. In Eclipse, right-click the `SmartStudy` project -> **Run As** -> **Run on Server**.
2. Select **Apache Tomcat v11.0** (or Tomcat 10) -> Click **Finish**.
3. Open your browser and navigate to:
   ```
   http://localhost:8080/SmartStudy/login.jsp
   ```
4. Sign in with `sharma@smartstudy.edu` / `pass123`.

---

### Method B: One-Click Terminal Launch (PowerShell / Windows CMD)

Make sure MySQL is running in XAMPP, then simply open a terminal in the project directory and run:

```powershell
.\run.ps1
```
*(Or in Windows Command Prompt: `.\run.bat`)*

The script automatically detects Java, compiles all sources using the local `.m2` repository JARs, and starts the JavaFX application.

---

### Method C: Standard Maven Command (Cross-Platform)

If you have Apache Maven installed on your system PATH:
```bash
mvn compile javafx:run
```

---

## 7. Teacher Portal: JavaFX vs. JSP/Servlet Architecture

### Why is there both a Desktop Client and a Web Portal?
- **Student Desktop Client (JavaFX)**: Students need a fast, reactive, responsive desktop productivity environment for timer presets, study tracking, and offline local TXT report generation.
- **Teacher / Admin Portal (Jakarta Servlets 6.0 + JSP 3.1)**: Faculty and administrators need a web browser portal to inspect whole student cohorts, monitor individual student completion rates, and review weekly reports without installing a desktop app.
- **Unified Architecture**: Both frontends connect to the same **Service Layer**, **DAO Layer**, and **MySQL Database**. When a student marks a task complete or logs a study session in the JavaFX desktop app, the faculty dashboard on the web portal immediately reflects the updated cohort metrics!

---

## 8. 3-Person Team Division & Presentation Guide

For university project evaluations with a 3-member team, here is the optimal workload and presentation division:

### 👤 Member 1: Core Architecture, OOP Design & Business Logic
* **Responsibilities**:
  * Object-Oriented Hierarchy (`User` abstract class, `Student`, `Teacher` subclasses).
  * Strategy Design Pattern (`PriorityStrategy`, `DeadlinePriorityStrategy`, `ExamPriorityStrategy`).
  * Custom User-Defined Exceptions (`InvalidTaskException`, `InvalidLoginException`).
  * In-memory Collections (`HashMap<Integer, Subject>`, `HashSet<String>` for unique topics, `PriorityQueue<Task>`).
* **Viva Questions to Answer**:
  * *"How does your Strategy Pattern work at runtime?"* -> Explain `RecommendationService.java` switching between deadline-based and exam-based algorithms.
  * *"Where are custom exceptions handled?"* -> Explain `TaskService.java` validating dates and `TaskController.java` catching `InvalidTaskException`.

### 👤 Member 2: JavaFX Desktop GUI, Multithreading & File I/O
* **Responsibilities**:
  * Modern Dark Glassmorphic JavaFX GUI (`FXML` layouts, CSS styling, table bindings).
  * Multithreading with `ThreadManager.java` (`javafx.concurrent.Task`, background `ExecutorService`).
  * Non-blocking Weekly Report Generator keeping the UI smooth during processing.
  * Java File I/O in `FileUtil.java` (`BufferedWriter` / `BufferedReader` writing & reading TXT reports).
* **Viva Questions to Answer**:
  * *"Why did you use multithreading for report generation?"* -> To avoid blocking the JavaFX Application Thread, keeping the progress bar animated and the UI responsive.
  * *"How does Launcher.java solve JavaFX module boot layer issues?"* -> It acts as a bootstrap class without extending `Application`, circumventing JavaFX module path checks.

### 👤 Member 3: Database, JDBC DAO Layer & Web Portal (Servlets + JSP)
* **Responsibilities**:
  * MySQL Database Schema design (6 tables, foreign keys, cascade constraints).
  * JDBC Persistence with DAO Pattern (`DBConnection.java`, `UserDAOImpl`, `TaskDAOImpl`, `SubjectDAOImpl`, etc.).
  * Jakarta Servlets (`LoginServlet`, `DashboardServlet`, `StudentServlet`, `ProgressServlet`, `ReportServlet`).
  * JSP templates with dynamic data rendering and session management.
* **Viva Questions to Answer**:
  * *"How do you prevent SQL Injection?"* -> PreparedStatement with parameterized queries in all DAO implementations.
  * *"How does the web portal calculate batch analytics?"* -> `DashboardServlet.java` queries all student records from MySQL and calculates cohort-wide averages.
