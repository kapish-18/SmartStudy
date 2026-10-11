---
project: SmartStudy
full_name: Intelligent Academic Planning & Progress Management System
document_type: project_plan_and_architecture
primary_language: Java
desktop_ui: JavaFX
database_connectivity: JDBC
database: MySQL
web_technology: Servlet + JSP
build_tool: Maven
---

SMARTSTUDY

Intelligent Academic Planning & Progress Management System

Java Course Project – Detailed Project Plan & Implementation Blueprint

Technology: Java, JavaFX, JDBC, MySQL, Servlets, JSP

## 1. Project Overview

SmartStudy is a simple academic planning and progress management system . The goal is to help a student manage subjects, assignments, exams, study tasks, study sessions, and academic progress from one application.

## 2. Project Goal

The system should allow a student to:

Maintain a list of subjects and their target marks.

Create and manage academic tasks and assignments.

Record upcoming exams and marks obtained.

Record study sessions and study time.

View subject-wise and overall academic progress.

Receive simple rule-based study recommendations.

Generate a weekly academic report.

Use a JavaFX desktop interface as the main student application.

Use a small JSP/Servlet web portal for teacher/admin viewing of student progress.

## 4. Overall System Design

```text
SMARTSTUDY
                              |
              +---------------+---------------+
              |                               |
       JavaFX Student App              JSP/Servlet Web Portal
              |                               |
              +---------------+---------------+
                              |
                       Service / Logic Layer
                              |
                         DAO Layer
                              |
                            JDBC
                              |
                           MySQL
```

The JavaFX application is the main student-facing application. The JSP/Servlet component is a small web portal that gives the project a natural use for Servlets and JSP. Both sides can use the same database and core business concepts.

## 5. Main Functional Modules

| Module | Purpose |

| --- | --- |

| 1. Login & User Management | Student/teacher login, basic role identification, and user information. |

| 2. Subject Management | Add, edit, delete, and view subjects, credits, target marks, and progress. |

| 3. Task & Assignment Management | Create tasks, set deadlines, priorities, estimated time, and completion status. |

| 4. Exam Management | Record exams, dates, target marks, and marks obtained. |

| 5. Study Session Tracker | Record what was studied, for which subject, and for how long. |

| 6. Dashboard & Progress | Display overall progress, subject progress, pending work, upcoming exams, and study hours. |

| 7. Recommendation System | Use simple rules to identify which subject/task needs attention. |

| 8. Report Generation | Generate a weekly text report using Java file handling and a background task. |

| 9. Teacher/Admin Web Portal | Servlet/JSP pages for viewing students and their academic progress. |



## 6. Main Student Workflow

Launch the SmartStudy JavaFX application.

Log in as a student.

Open the dashboard.

Add subjects such as DBMS, DAA, Java, and Computer Networks.

Add exams and assignments for those subjects.

Create study tasks with deadlines, priority, and estimated study time.

Complete tasks and record study sessions.

View updated progress and recommendations.

Generate a weekly report.

The report generation runs as a background operation so the JavaFX interface remains responsive.

## 7. Required Java Concepts – Exact Mapping

| Concept | Implementation | Location |

| --- | --- | --- |

| Inheritance | User -> Student and User -> Teacher | Model package |

| Polymorphism | Different PriorityStrategy implementations override calculatePriority() | Strategy/service layer |

| Abstract Class | User and/or ReportGenerator declared abstract | Model/report package |

| Packages | model, dao, service, exception, util, javafx, servlet, report | Entire project |

| User-defined Exception | InvalidTaskException for invalid deadlines/task data | exception package |

| File Handling | Write and read weekly academic report text files | FileUtil/report package |

| Collections | ArrayList for tasks, HashMap for subject lookup, HashSet for unique topics, PriorityQueue for urgent tasks | Service/collections |

| JavaFX | Main desktop student application | javafx package + FXML |

| JDBC | DAO classes communicate with MySQL | dao/util |

| Servlets | Handle web requests for teacher/admin portal | web/servlet |

| JSP | Render teacher/admin web pages | webapp |

| Multithreading | Background weekly report generation | javafx/threading |



## 8. Object-Oriented Design

### 8.1 Abstract Class and Inheritance

abstract class User {
    protected int id;
    protected String name;
    protected String email;

    public abstract String getDashboardType();
}

class Student extends User {
    @Override
    public String getDashboardType() {
        return "Student Dashboard";
    }
}

class Teacher extends User {
    @Override
    public String getDashboardType() {
        return "Teacher Dashboard";
    }
}

This gives a clear demonstration of abstraction, inheritance, method overriding, and runtime polymorphism.

### 8.2 Polymorphism Through Strategy

abstract class PriorityStrategy {
    public abstract int calculatePriority(Task task);
}

class DeadlinePriorityStrategy extends PriorityStrategy {
    @Override
    public int calculatePriority(Task task) {
        // priority based mainly on deadline
    }
}

class ExamPriorityStrategy extends PriorityStrategy {
    @Override
    public int calculatePriority(Task task) {
        // priority based on upcoming exam
    }
}

The RecommendationService can hold a PriorityStrategy reference and call calculatePriority() without needing to know which implementation is being used.

## 9. Simple Intelligent Recommendation Logic

| Condition | Recommendation |

| --- | --- |

| Exam within 3 days + progress below 60% | High priority recommendation |

| Assignment deadline within 2 days | High priority recommendation |

| Task deadline has passed | Overdue / critical task |

| Subject progress is below target | Increase subject priority |

| No study session recorded recently | Suggest studying that subject |

| Subject is already above target and has no urgent work | Lower priority |



## 10. Collections Usage

| Collection | Purpose | Example Location |

| --- | --- | --- |

| ArrayList<Task> | Store a list of tasks fetched from the database. | TaskService |

| HashMap<Integer, Subject> | Quickly find a subject by subject ID. | SubjectService |

| HashSet<String> | Maintain unique completed topics. | ProgressService |

| PriorityQueue<Task> | Keep the most urgent tasks at the front. | TaskPriorityQueue |



## 11. Exception Handling

User-defined exceptions should be small and meaningful.

public class InvalidTaskException extends Exception {
    public InvalidTaskException(String message) {
        super(message);
    }
}

Example use: when a user creates a task whose deadline is before the current date, TaskService throws InvalidTaskException and the JavaFX controller displays a simple error message.

## 12. File Handling

The system should generate a weekly report such as:

reports/
    weekly_report_2026_10_06.txt

The report can contain:

Student name

Total study hours

Completed and pending tasks

Subject-wise progress

Upcoming exams

Top recommendations

FileUtil should demonstrate both writing the report and reading an existing report when the user selects 'View Previous Report'.

## 13. Multithreading Operation

```text
JavaFX UI Thread
      |
      +---- start background Task
                    |
                    +-- fetch data
                    +-- calculate statistics
                    +-- generate report
                    +-- write TXT file
                    |
                    +-- update UI when finished
```

This demonstrates why multithreading is useful: a long-running operation should not freeze the JavaFX user interface.

## 14. JavaFX Screens

| Screen | Main Contents |

| --- | --- |

| Login | Email/password fields and login button. |

| Dashboard | Overall progress, today's tasks, upcoming exams, study hours, recommendation. |

| Subjects | Table of subjects with add/edit/delete operations. |

| Tasks | Task table, filters, add/edit/delete, completion status. |

| Exams | Upcoming exams and marks. |

| Study Session | Select subject/topic and record study duration. |

| Progress | Subject-wise and overall progress. |

| Reports | Generate and view weekly text reports. |



## 15. JSP + Servlet Web Portal

| JSP | Purpose | Servlet |

| --- | --- | --- |

| login.jsp | Login page | LoginServlet |

| dashboard.jsp | Teacher/admin summary | DashboardServlet |

| students.jsp | List of students | StudentServlet |

| student-progress.jsp | Selected student's subjects/progress | ProgressServlet |

| reports.jsp | View basic report information | ReportServlet |



The servlet receives the browser request, calls the service/DAO layer, places data into request attributes, and forwards to the JSP. JSP is responsible for displaying the data.

## 16. Recommended Project Structure

```text
SmartStudy/
|
+-- pom.xml
|
+-- src/main/java/com/smartstudy/
|   |
|   +-- Main.java
|   |
|   +-- model/
|   |   +-- User.java
|   |   +-- Student.java
|   |   +-- Teacher.java
|   |   +-- Subject.java
|   |   +-- Task.java
|   |   +-- Exam.java
|   |   +-- Assignment.java
|   |   +-- StudySession.java
|   |
|   +-- dao/
|   |   +-- UserDAO.java
|   |   +-- SubjectDAO.java
|   |   +-- TaskDAO.java
|   |   +-- ExamDAO.java
|   |   +-- StudySessionDAO.java
|   |
|   +-- dao/impl/
|   |   +-- UserDAOImpl.java
|   |   +-- SubjectDAOImpl.java
|   |   +-- TaskDAOImpl.java
|   |   +-- ExamDAOImpl.java
|   |   +-- StudySessionDAOImpl.java
|   |
|   +-- service/
|   |   +-- UserService.java
|   |   +-- SubjectService.java
|   |   +-- TaskService.java
|   |   +-- ProgressService.java
|   |   +-- RecommendationService.java
|   |
|   +-- strategy/
|   |   +-- PriorityStrategy.java
|   |   +-- DeadlinePriorityStrategy.java
|   |   +-- ExamPriorityStrategy.java
|   |
|   +-- exception/
|   |   +-- InvalidTaskException.java
|   |   +-- InvalidLoginException.java
|   |
|   +-- util/
|   |   +-- DBConnection.java
|   |   +-- FileUtil.java
|   |   +-- ValidationUtil.java
|   |
|   +-- collections/
|   |   +-- TaskPriorityQueue.java
|   |
|   +-- report/
|   |   +-- ReportGenerator.java
|   |   +-- StudentReportGenerator.java
|   |
|   +-- javafx/
|       +-- controller/
|       |   +-- LoginController.java
|       |   +-- DashboardController.java
|       |   +-- SubjectController.java
|       |   +-- TaskController.java
|       |   +-- ExamController.java
|       |   +-- StudyController.java
|       |   +-- ProgressController.java
|       |
|       +-- ThreadManager.java
|
+-- src/main/resources/
|   +-- fxml/
|   |   +-- login.fxml
|   |   +-- dashboard.fxml
|   |   +-- subjects.fxml
|   |   +-- tasks.fxml
|   |   +-- exams.fxml
|   |   +-- study.fxml
|   |   +-- progress.fxml
|   |
|   +-- css/
|       +-- style.css
|
+-- src/main/webapp/
    +-- WEB-INF/web.xml
    +-- login.jsp
    +-- dashboard.jsp
    +-- students.jsp
    +-- student-progress.jsp
    +-- reports.jsp
    +-- css/web-style.css
```

## 17. Important File Responsibilities

| File/Class | Responsibility |

| --- | --- |

| Main.java | Starts the JavaFX application. |

| User.java | Abstract parent class demonstrating abstraction. |

| Student.java / Teacher.java | Inheritance and method overriding. |

| Subject.java / Task.java / Exam.java / StudySession.java | Domain/model classes. |

| DAO interfaces | Define database operations without SQL in controllers. |

| DAO implementations | Perform JDBC SQL operations. |

| DBConnection.java | Creates JDBC database connections. |

| Service classes | Business logic and validation. |

| RecommendationService.java | Rule-based intelligent recommendations. |

| PriorityStrategy classes | Polymorphism and task-priority calculation. |

| InvalidTaskException.java | User-defined exception. |

| FileUtil.java | Read/write report files. |

| ReportGenerator.java | Abstract report generation design. |

| JavaFX Controllers | Respond to UI actions and call services. |

| ThreadManager.java | Runs report generation in the background. |

| Servlet classes | Handle web requests and connect the web layer to services. |

| JSP pages | Display teacher/admin web pages. |



## 18. Basic Database Design

| Table | Important Columns |

| --- | --- |

| users | id, name, email, password, role |

| subjects | id, student_id, name, course_code, credits, target_marks |

| tasks | id, student_id, subject_id, title, description, priority, deadline, status, estimated_minutes |

| exams | id, subject_id, exam_name, exam_date, target_marks, marks_obtained |

| assignments | id, subject_id, title, deadline, status, marks |

| study_sessions | id, student_id, subject_id, topic, duration_minutes, session_date |



## 19. JDBC / DAO Flow

```text
JavaFX Controller
       |
       v
TaskService
       |
       v
TaskDAO
       |
       v
TaskDAOImpl
       |
       v
DBConnection
       |
       v
MySQL
```

## 20. Implementation Plan – Step by Step

### Phase 1 – Setup

Create Maven project.

Configure JavaFX dependencies.

Configure MySQL/JDBC driver.

Create package structure.

Create initial database.

### Phase 2 – Core OOP

Create User abstract class.

Create Student and Teacher subclasses.

Create Subject, Task, Exam, Assignment, and StudySession classes.

Implement basic inheritance and polymorphism.

### Phase 3 – JDBC

Create DBConnection.

Create DAO interfaces.

Create DAO implementations.

Test insert, update, delete, and select operations.

### Phase 4 – Services

Create service classes.

Add validation.

Add InvalidTaskException.

Implement collection-based task/subject handling.

### Phase 5 – JavaFX

Build Login screen.

Build Dashboard.

Build Subject, Task, Exam, Study Session, and Progress screens.

Connect buttons/forms to service methods.

### Phase 6 – Intelligence

Implement PriorityStrategy.

Implement recommendation rules.

Show highest-priority tasks on dashboard.

### Phase 7 – File Handling + Threads

Generate weekly TXT report.

Add previous-report reading.

Move report generation to a background Task/ExecutorService.

Show progress/status in JavaFX.

### Phase 8 – JSP + Servlets

Create simple login servlet.

Create student listing servlet.

Create progress servlet.

Create JSP pages for display.

### Phase 9 – Testing & Documentation

Test every CRUD operation.

Test invalid input and custom exceptions.

Test report generation.

Test web portal.

Prepare screenshots, class diagram, database diagram, and viva explanation.

## 21. Minimum Testing Checklist

Valid and invalid login.

Add/edit/delete subject.

Add task with valid deadline.

Try task with past deadline and verify InvalidTaskException.

Mark task complete and verify progress update.

Add exam and display upcoming exam.

Record study session and verify study hours.

Verify recommendation changes based on deadlines/progress.

Generate report and verify TXT file.

Open/read a previous report.

Verify background report generation does not freeze the JavaFX UI.

Open JSP/Servlet portal and view student data.

## 24. Expected Technologies / Dependencies

| Technology | Use |

| --- | --- |

| Java | Main programming language. |

| JavaFX | Desktop GUI. |

| FXML | JavaFX UI layout. |

| MySQL | Relational database. |

| JDBC Driver | Java-to-MySQL connectivity. |

| Servlet API | Web request handling. |

| JSP | Web page rendering. |

| Maven | Project/dependency management. |

| Tomcat or equivalent servlet container | Run JSP/Servlet web module. |



## 25. Final Recommended Scope

A working JavaFX student application.

A working MySQL database connected through JDBC.

CRUD operations for subjects/tasks/exams.

Academic progress calculation.

A simple rule-based recommendation system.

At least one clear inheritance hierarchy.

At least one clear polymorphism example.

An abstract class.

A user-defined exception that is actually triggered.

Meaningful use of Java Collections.

TXT file creation and reading.

A background multithreading operation.

A small working Servlet + JSP web portal.

A clean package structure and understandable code.


## AI-Readable Project Constraints and Architecture Summary

### Core Architecture
- JavaFX is the main student-facing desktop application.
- JSP and Servlets provide a browser-based teacher/admin portal.
- Both interfaces access shared business logic and the same MySQL database.
- JDBC database operations are isolated in DAO implementations.
- Controllers call service classes; controllers do not contain SQL queries.
- Services implement validation, progress calculations, recommendation logic, and coordination of DAO operations.
- Models represent application entities.

### Required Java Concepts Checklist
- [ ] Inheritance: `Student extends User`, `Teacher extends User`
- [ ] Polymorphism: multiple implementations of `PriorityStrategy.calculatePriority(Task)`
- [ ] Abstract class: `User` and/or `ReportGenerator`
- [ ] Packages: `model`, `dao`, `dao.impl`, `service`, `strategy`, `exception`, `util`, `collections`, `report`, `javafx`
- [ ] User-defined exception: `InvalidTaskException`
- [ ] File handling: write and read weekly TXT reports
- [ ] Collections: `ArrayList`, `HashMap`, `HashSet`, `PriorityQueue`
- [ ] JavaFX: desktop GUI using FXML and controllers
- [ ] JDBC: database operations through DAO implementations
- [ ] Servlets: process requests for web portal
- [ ] JSP: render web pages
- [ ] Multithreading: background report generation using JavaFX `Task` or `ExecutorService`

### Data Flow
1. JavaFX controller receives a user action.
2. Controller calls the relevant service.
3. Service validates input and applies business rules.
4. Service calls a DAO interface.
5. DAO implementation executes SQL using JDBC.
6. MySQL returns data or confirms the operation.
7. Service returns a result to the controller.
8. Controller updates the JavaFX interface.

For the web portal:
1. Browser requests a route.
2. Servlet processes the request.
3. Servlet calls a service.
4. Service calls DAO/JDBC as needed.
5. Servlet places results in request attributes.
6. Servlet forwards to a JSP page for rendering.

### Key Implementation Rules
- Use prepared statements for SQL operations.
- Keep SQL out of JavaFX controllers and JSP files.
- Keep JSP focused on presentation and Servlets focused on request handling.
- Use a background task for report generation so the JavaFX UI thread remains responsive.
- Validate task deadlines and required fields before database insertion.
- Keep the database schema aligned with the implemented screens and features.
- Store database configuration outside source code where practical.
