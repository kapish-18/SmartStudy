# 🎓 SmartStudy — Comprehensive Viva & Technical Defense Guide

> **Project Name**: SmartStudy: Intelligent Academic Planning & Progress Management System  
> **Architecture**: Dual-Frontend (JavaFX Desktop + Jakarta Servlets/JSP Web Portal) with Shared Core Service, Strategy, DAO, and MySQL Database Layer  
> **Team Members**:
> - **Member A (Kapish Tickoo)**: Backend Architecture, Core Business Logic, Strategy Pattern & OOP Hierarchy
> - **Member B (Manit)**: JavaFX Desktop Application, Multithreading, File I/O & Reactive GUI
> - **Member C (Deepujjwal)**: MySQL Database, JDBC DAO Layer, Jakarta Servlets 6.0 & JSP 3.1 Web Portal

---

# 📑 TABLE OF CONTENTS
1. [PART 1: Team Overview & Curriculum Concepts Mapping (Read by ALL)](#part-1-team-overview--curriculum-concepts-mapping)
2. [PART 2: Member A — Kapish Tickoo (Backend & Core Functionality)](#part-2-member-a--kapish-tickoo-backend--core-functionality)
3. [PART 3: Member B — Manit (JavaFX Desktop GUI, Multithreading & File I/O)](#part-3-member-b--manit-javafx-desktop-gui-multithreading--file-io)
4. [PART 4: Member C — Deepujjwal (Database, JDBC DAO & Teacher Web Portal)](#part-4-member-c--deepujjwal-database-jdbc-dao--teacher-web-portal)

---

# PART 1: Team Overview & Curriculum Concepts Mapping
*(Every team member must read and understand this section before the evaluation)*

### 1.1 Project Purpose & Problem Statement
Engineering students juggle multiple demanding courses, assignments with competing deadlines, and upcoming midterms/CATs. Typical to-do list apps are static; they don't dynamically adapt to academic priorities.  
**SmartStudy** solves this through an **intelligent, rule-based recommendation and scheduling system** with dual interfaces:
- **For Students**: A responsive JavaFX desktop application with study session timers, urgent priority queues, and non-blocking weekly report generation.
- **For Faculty/Teachers**: A web portal (Servlets + JSP) to monitor entire student cohorts, inspect individual student subject progress, and review generated weekly reports.

### 1.2 Dual-Tier Architecture Diagram
```text
                          +------------------------------------------------+
                          |                   SMARTSTUDY                   |
                          +-----------------------+------------------------+
                                                  |
                  +-------------------------------+-------------------------------+
                  |                                                               |
     [Desktop Client: JavaFX]                                       [Web Portal: JSP + Servlets]
         Presented by MANIT                                            Presented by DEEPUJJWAL
   FXML, Controllers, CSS, Threading                             Jakarta Servlets 6.0, JSP 3.1, Tomcat
                  |                                                               |
                  +-------------------------------+-------------------------------+
                                                  |
                                       +--------------------+
                                       |   Service Layer    |  Presented by
                                       +--------------------+  KAPISH TICKOO
                                       |  Strategy Pattern  |
                                       |  Collections API   |
                                       |  Custom Exceptions |
                                       |  Recommendation    |
                                       +---------+----------+
                                                 |
                                       +--------------------+
                                       |     DAO Layer      |  Presented by
                                       +--------------------+  DEEPUJJWAL
                                       | UserDAO, TaskDAO   |
                                       | ExamDAO, SubjectDAO|
                                       +---------+----------+
                                                 |
                                       +--------------------+
                                       |    MySQL Server    |
                                       | (XAMPP Port 3306)  |
                                       +--------------------+
```

### 1.3 Master Java Curriculum Mapping
| Curriculum Topic | Exact Code Implementation | Primary Presenter |
| :--- | :--- | :--- |
| **Abstraction** | `abstract class User`, `abstract class ReportGenerator` | **Kapish Tickoo** |
| **Inheritance** | `Student extends User`, `Teacher extends User` | **Kapish Tickoo** |
| **Polymorphism & Strategy Pattern** | `PriorityStrategy` interface implemented by `DeadlinePriorityStrategy` and `ExamPriorityStrategy` | **Kapish Tickoo** |
| **User-Defined Exceptions** | `InvalidTaskException`, `InvalidLoginException` | **Kapish Tickoo** |
| **Collections Framework** | `HashMap<Integer, Subject>`, `HashSet<String>`, `PriorityQueue<Task>`, `ArrayList<Task>` | **Kapish Tickoo** |
| **Multithreading** | `ThreadManager` running `javafx.concurrent.Task` on background `ExecutorService` | **Manit** |
| **Java File I/O** | `FileUtil` (`BufferedWriter` / `BufferedReader`) saving & parsing TXT reports | **Manit** |
| **JavaFX Desktop GUI** | FXML layouts, MVC controllers, CSS glassmorphic theme, root-swapping | **Manit** |
| **JDBC & DAO Pattern** | `DBConnection`, PreparedStatement queries, `UserDAOImpl`, `TaskDAOImpl`, etc. | **Deepujjwal** |
| **Servlets & JSP Web Portal** | Jakarta Servlets (`@WebServlet`), JSP pages, session tracking, cohort metrics | **Deepujjwal** |
| **Relational Database** | MySQL schema (6 tables, foreign keys, cascades, multi-student seed data) | **Deepujjwal** |

---

# PART 2: Member A — Kapish Tickoo (Backend & Core Functionality)

### 2.1 Role & Scope
You are responsible for explaining the **Core Architecture, Object-Oriented Design, Strategy Pattern, Custom Exceptions, and Java Collections Framework**. You show how real business rules govern tasks, subjects, users, and priorities before anything reaches the UI or database.

### 2.2 File Breakdown & Responsibilities
| File Path | Purpose |
| :--- | :--- |
| [`src/main/java/com/smartstudy/model/User.java`](src/main/java/com/smartstudy/model/User.java) | Abstract root user class encapsulating `id`, `name`, `email`, `password`, `role`. |
| [`src/main/java/com/smartstudy/model/Student.java`](src/main/java/com/smartstudy/model/Student.java) | Concrete subclass extending `User`, adds `rollNumber` and overrides abstract methods. |
| [`src/main/java/com/smartstudy/model/Teacher.java`](src/main/java/com/smartstudy/model/Teacher.java) | Concrete subclass extending `User`, defines teacher role and dashboard behavior. |
| [`src/main/java/com/smartstudy/model/Subject.java`](src/main/java/com/smartstudy/model/Subject.java) | Model representing university courses (code, credits, target marks). |
| [`src/main/java/com/smartstudy/model/Task.java`](src/main/java/com/smartstudy/model/Task.java) | Task entity implementing `Comparable<Task>` for sorting. |
| [`src/main/java/com/smartstudy/strategy/PriorityStrategy.java`](src/main/java/com/smartstudy/strategy/PriorityStrategy.java) | Strategy base interface defining `int calculatePriority(Task task)`. |
| [`src/main/java/com/smartstudy/strategy/DeadlinePriorityStrategy.java`](src/main/java/com/smartstudy/strategy/DeadlinePriorityStrategy.java) | Priority algorithm based on days remaining until due date. |
| [`src/main/java/com/smartstudy/strategy/ExamPriorityStrategy.java`](src/main/java/com/smartstudy/strategy/ExamPriorityStrategy.java) | Priority algorithm giving boosts to tasks tied to upcoming exams. |
| [`src/main/java/com/smartstudy/collections/TaskPriorityQueue.java`](src/main/java/com/smartstudy/collections/TaskPriorityQueue.java) | Custom wrapper around `java.util.PriorityQueue<Task>`. |
| [`src/main/java/com/smartstudy/exception/InvalidTaskException.java`](src/main/java/com/smartstudy/exception/InvalidTaskException.java) | Custom checked exception for task validation failures. |
| [`src/main/java/com/smartstudy/exception/InvalidLoginException.java`](src/main/java/com/smartstudy/exception/InvalidLoginException.java) | Custom checked exception for authentication failures. |
| [`src/main/java/com/smartstudy/service/SubjectService.java`](src/main/java/com/smartstudy/service/SubjectService.java) | Manages subjects with in-memory `HashMap` caching. |
| [`src/main/java/com/smartstudy/service/TaskService.java`](src/main/java/com/smartstudy/service/TaskService.java) | Task business logic, validation rules, and status filtering. |
| [`src/main/java/com/smartstudy/service/ProgressService.java`](src/main/java/com/smartstudy/service/ProgressService.java) | Calculates completion percentages and uses `HashSet` for unique topics. |
| [`src/main/java/com/smartstudy/service/RecommendationService.java`](src/main/java/com/smartstudy/service/RecommendationService.java) | Rule engine applying Strategy pattern to recommend tasks. |
| [`src/main/java/com/smartstudy/service/UserService.java`](src/main/java/com/smartstudy/service/UserService.java) | Handles user authentication and retrieval. |

---

### 2.3 Detailed Java Concepts & How They Work in Code

#### A. Abstraction & Inheritance (OOP)
- **`User` is an `abstract` class**: It cannot be instantiated directly. It defines common fields (`id`, `name`, `email`, `password`, `role`) and an abstract method:
  ```java
  public abstract String getDashboardType();
  ```
- **`Student extends User`**: Implements `getDashboardType()` returning `"Student Dashboard"`, adds `rollNumber`, and overrides `getWelcomeMessage()`.
- **`Teacher extends User`**: Implements `getDashboardType()` returning `"Faculty Dashboard"`.
- **Runtime Polymorphism**: When the DAO loads a user from MySQL, it checks `role` column and returns `new Student()` or `new Teacher()`. Code calling `user.getDashboardType()` executes the subclass's version at runtime!

#### B. Strategy Design Pattern & Polymorphism
- **Problem**: Academic priority changes based on context. If an exam is in 3 days, exam-related coursework is more critical than a general homework due in 5 days.
- **Solution**: The **Strategy Pattern**.
  - `PriorityStrategy` defines the contract: `int calculatePriority(Task task, ...)`
  - `DeadlinePriorityStrategy`: Calculates days left (`ChronoUnit.DAYS.between(today, deadline)`). Tasks due within 2 days get Priority 1 (Urgent).
  - `ExamPriorityStrategy`: Inspects upcoming exams for that subject. If an exam is scheduled within 7 days, task priority is boosted to High (Priority 1) regardless of normal due date.
- **Runtime Switching**: In `RecommendationService.java`, the engine evaluates conditions and dynamically executes the appropriate strategy.

#### C. User-Defined Exceptions
- **`InvalidTaskException`**: Extends `Exception` (checked exception).
  - In `TaskService.java`:
    ```java
    if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
        throw new InvalidTaskException("Task title cannot be empty.");
    }
    if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDate.now())) {
        throw new InvalidTaskException("Task deadline cannot be in the past: " + task.getDeadline());
    }
    ```
- **`InvalidLoginException`**: Thrown in `UserService.java` when bad email/password is entered or email is blank. Caught by `LoginController` and `LoginServlet` to display red alerts.

#### D. Collections Framework Usage
1. **`PriorityQueue<Task>`** (in `TaskPriorityQueue.java`):
   - Orders tasks by computed priority score so the most critical tasks always emerge first (`peek()` / `poll()`).
2. **`HashMap<Integer, Subject>`** (in `SubjectService.java`):
   - In-memory cache mapping `subjectId -> Subject`. Provides $O(1)$ constant time lookup when mapping tasks to subject names.
3. **`HashSet<String>`** (in `ProgressService.java`):
   - Used in `getCompletedTopics(studentId)`. When a student logs 5 sessions on "SQL Joins", the `HashSet` automatically eliminates duplicates, returning the exact count of *unique* topics studied.
4. **`ArrayList<Task>`** (in `TaskService.java`):
   - Dynamic collection used for sorting, filtering pending vs. completed tasks, and passing records to UI tables.

---

### 2.4 Evaluator / Teacher Viva Q&A (Kapish Tickoo)

**Q1: Why did you declare `User` as an abstract class instead of an interface?**  
*Answer*: "`User` represents an identity with shared concrete state—every user has an ID, name, email, password, and role, along with common getters and setters. An interface cannot hold instance state. By making `User` abstract, we share common attributes while enforcing that subclasses (`Student`, `Teacher`) provide their own specific implementations for `getDashboardType()`."

**Q2: How does the Strategy pattern work in your recommendation engine?**  
*Answer*: "Instead of hardcoding a nested `if-else` block to decide task importance, we created a `PriorityStrategy` interface. `DeadlinePriorityStrategy` prioritizes by deadline proximity, while `ExamPriorityStrategy` prioritizes based on upcoming exam dates. `RecommendationService` selects and executes the appropriate strategy at runtime, adhering to the Open/Closed Principle—we can add a new strategy (e.g., CreditWeightPriorityStrategy) without modifying existing code."

**Q3: Where and why do you throw `InvalidTaskException`?**  
*Answer*: "In `TaskService.java`, when creating or editing a task, business rules require that a task must have a title and its deadline cannot be in the past. If either check fails, we throw our custom checked exception `InvalidTaskException`. The caller (e.g. `TaskController`) catches it and gracefully alerts the user without crashing the system."

**Q4: Why use a `HashSet` for unique topics in `ProgressService`?**  
*Answer*: "Students often log multiple study sessions for the same topic across different days (e.g., 'Normalization' on Monday and Wednesday). If we used a regular `List`, duplicate topics would artificially inflate the topics studied count. A `HashSet` guarantees uniqueness by hashing strings, providing an accurate count of unique topics mastered."

---

# PART 3: Member B — Manit (JavaFX Desktop GUI, Multithreading & File I/O)

### 3.1 Role & Scope
You are responsible for explaining the **Student Desktop Interface (JavaFX)**, the **MVC Controller architecture**, **Multithreading with background tasks**, **Java File I/O for report generation**, and how the GUI stays responsive without freezing.

### 3.2 File Breakdown & Responsibilities
| File Path | Purpose |
| :--- | :--- |
| [`src/main/java/com/smartstudy/Launcher.java`](src/main/java/com/smartstudy/Launcher.java) | Bootstrap entry point that delegates to `Main.java` to bypass JavaFX module path restrictions. |
| [`src/main/java/com/smartstudy/Main.java`](src/main/java/com/smartstudy/Main.java) | JavaFX Application class managing stage, scenes, and root swapping. |
| [`src/main/java/com/smartstudy/javafx/ThreadManager.java`](src/main/java/com/smartstudy/javafx/ThreadManager.java) | Multithreading manager holding an `ExecutorService` thread pool. |
| [`src/main/java/com/smartstudy/util/FileUtil.java`](src/main/java/com/smartstudy/util/FileUtil.java) | Java File I/O helper reading and writing weekly reports using `BufferedWriter`/`BufferedReader`. |
| [`src/main/java/com/smartstudy/report/ReportGenerator.java`](src/main/java/com/smartstudy/report/ReportGenerator.java) | Abstract report template defining report header/footer generation. |
| [`src/main/java/com/smartstudy/report/StudentReportGenerator.java`](src/main/java/com/smartstudy/report/StudentReportGenerator.java) | Concrete generator producing formatted weekly TXT report. |
| [`src/main/java/com/smartstudy/javafx/controller/LoginController.java`](src/main/java/com/smartstudy/javafx/controller/LoginController.java) | JavaFX login screen controller with real DB authentication. |
| [`src/main/java/com/smartstudy/javafx/controller/DashboardController.java`](src/main/java/com/smartstudy/javafx/controller/DashboardController.java) | KPI metrics, intelligent recommendations, and background report button. |
| [`src/main/java/com/smartstudy/javafx/controller/SubjectController.java`](src/main/java/com/smartstudy/javafx/controller/SubjectController.java) | Manages enrolled courses with live TableView binding. |
| [`src/main/java/com/smartstudy/javafx/controller/TaskController.java`](src/main/java/com/smartstudy/javafx/controller/TaskController.java) | Task management table, status filters, and `InvalidTaskException` demo button. |
| [`src/main/java/com/smartstudy/javafx/controller/ExamController.java`](src/main/java/com/smartstudy/javafx/controller/ExamController.java) | Schedules exams, sets target marks, and records actual grades. |
| [`src/main/java/com/smartstudy/javafx/controller/StudyController.java`](src/main/java/com/smartstudy/javafx/controller/StudyController.java) | Logs study sessions with Pomodoro presets (25m, 60m, 90m). |
| [`src/main/java/com/smartstudy/javafx/controller/ProgressController.java`](src/main/java/com/smartstudy/javafx/controller/ProgressController.java) | Displays academic progress bars and unique topic counts. |
| [`src/main/resources/fxml/`](src/main/resources/fxml/) | 7 FXML layout files defining the visual structure. |
| [`src/main/resources/css/style.css`](src/main/resources/css/style.css) | Custom modern dark glassmorphic styling. |

---

### 3.3 Detailed Technical Concepts & How They Work in Code

#### A. Multithreading (`ThreadManager` & `javafx.concurrent.Task`)
- **Problem**: Generating a weekly report involves fetching subjects, querying completed tasks, computing completion rates, and writing a file to disk. If run on the main thread, the JavaFX Application Thread freezes: the mouse cursor hangs, animations stop, and the OS marks the window "Not Responding".
- **Solution**:
  - We use `ThreadManager.java` which maintains a daemon `ExecutorService`:
    ```java
    private final ExecutorService executor = Executors.newFixedThreadPool(3, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true); // Shuts down automatically when app exits
        return t;
    });
    ```
  - In `DashboardController.java`, we create a `javafx.concurrent.Task<String>`:
    - `call()` runs on a **background worker thread** (computes metrics, simulates steps with `updateProgress()`, and calls `FileUtil.writeReport()`).
    - `setOnSucceeded()` runs automatically back on the **JavaFX Application Thread** once the task completes, updating the UI label and displaying the report in the preview box.
  - Result: The animated progress bar moves smoothly while the UI remains 100% interactive!

#### B. Java File I/O (`FileUtil.java` & `ReportGenerator.java`)
- **Writing**: `FileUtil.writeReport(fileName, content)`:
  - Uses `BufferedWriter` wrapping a `FileWriter`.
  - Writes the generated TXT report into the `reports/` folder.
  - Automatically ensures the target directory exists (`Files.createDirectories()`).
- **Reading**: `FileUtil.readReport(fileName)`:
  - Uses `BufferedReader` wrapping a `FileReader` with a `StringBuilder` in a `while ((line = reader.readLine()) != null)` loop.
  - Throws `FileNotFoundException` if missing.
- **Listing**: `FileUtil.listReports()`:
  - Uses `Files.newDirectoryStream(dir, "*.txt")` to return all available reports for user selection.

#### C. JavaFX Architecture & Seamless Fullscreen Navigation
- **Bootstrap Pattern**: `Launcher.java` has a standard `main(String[] args)` that calls `Main.main(args)`. Because `Launcher` does not inherit from `Application`, it bypasses the Java 9+ module-path boot-layer restriction, allowing the app to run as a standard classpath application in Eclipse and CMD.
- **Root-Swapping Scene Navigation**:
  - To prevent window flickering and avoid collapsing out of fullscreen/maximized state, `Main.switchScene(fxmlFile)` does not create a new `Scene(root, 900, 600)`.
  - Instead, it checks `primaryStage.getScene()` and executes `currentScene.setRoot(newRoot)`. The window retains its exact size, screen coordinates, and fullscreen state!

---

### 3.4 Evaluator / Teacher Viva Q&A (Manit)

**Q1: Why does your application use `Launcher.java` instead of directly launching `Main.java`?**  
*Answer*: "In modern JDKs (JDK 11+), launching a class that directly extends `javafx.application.Application` from a non-modular classpath causes a `java.lang.module.FindException: Module javafx.web/graphics not found` boot-layer error. `Launcher.java` is a plain Java class without JavaFX inheritance. When JVM starts `Launcher.main()`, it loads normally, and then delegates to `Application.launch(Main.class)`, completely resolving the classpath module restriction."

**Q2: How do you achieve non-blocking report generation using multithreading?**  
*Answer*: "JavaFX has a single-threaded UI model called the JavaFX Application Thread. If long-running tasks like File I/O or database aggregations run on this thread, the UI freezes. In `DashboardController.java`, we instantiate a `javafx.concurrent.Task<String>` and submit it to an `ExecutorService` managed by `ThreadManager.java`. The `call()` method executes on a background worker thread. When finished, `setOnSucceeded()` notifies the JavaFX thread safely to update the UI without blocking."

**Q3: How is File I/O implemented when saving reports?**  
*Answer*: "In `FileUtil.java`, we use `BufferedWriter` and `FileWriter` wrapped inside a try-with-resources statement to guarantee that file handles are safely closed even if an exception occurs. The report generator builds a timestamped report containing overall progress, subject completion, and urgent recommendations, and writes it to disk under the `reports/` folder."

**Q4: How does your UI adapt when the user maximizes or resizes the window?**  
*Answer*: "Our FXML layouts use responsive layout containers—`BorderPane`, `VBox`, and `HBox`—with `HBox.hgrow="ALWAYS"` and percentage constraints. In `Main.java`, when switching scenes, we call `currentScene.setRoot(root)` instead of creating a new `Scene`. This preserves the existing stage dimensions, maximized state, and fullscreen mode without collapsing to a corner."

---

# PART 4: Member C — Deepujjwal (Database, JDBC DAO & Teacher Web Portal)

### 4.1 Role & Scope
You are responsible for explaining the **MySQL Relational Database design**, the **JDBC Persistence Layer using the DAO Design Pattern**, and the **Teacher Web Portal built with Jakarta Servlets 6.0 and JSP 3.1** running on Apache Tomcat.

### 4.2 File Breakdown & Responsibilities
| File Path | Purpose |
| :--- | :--- |
| [`database/schema.sql`](database/schema.sql) | DDL schema script creating 6 relational tables with foreign keys and multi-student seed data. |
| [`src/main/java/com/smartstudy/util/DBConnection.java`](src/main/java/com/smartstudy/util/DBConnection.java) | Singleton connection factory managing JDBC MySQL connections. |
| [`src/main/java/com/smartstudy/dao/UserDAO.java`](src/main/java/com/smartstudy/dao/UserDAO.java) | Interface defining user CRUD operations and `findByRole(role)`. |
| [`src/main/java/com/smartstudy/dao/impl/UserDAOImpl.java`](src/main/java/com/smartstudy/dao/impl/UserDAOImpl.java) | JDBC implementation using `PreparedStatement` and result mapping. |
| [`src/main/java/com/smartstudy/dao/SubjectDAO.java`](src/main/java/com/smartstudy/dao/SubjectDAO.java) / [`SubjectDAOImpl.java`](src/main/java/com/smartstudy/dao/impl/SubjectDAOImpl.java) | Database access for enrolled subjects. |
| [`src/main/java/com/smartstudy/dao/TaskDAO.java`](src/main/java/com/smartstudy/dao/TaskDAO.java) / [`TaskDAOImpl.java`](src/main/java/com/smartstudy/dao/impl/TaskDAOImpl.java) | Database access for student tasks, priorities, and deadlines. |
| [`src/main/java/com/smartstudy/dao/ExamDAO.java`](src/main/java/com/smartstudy/dao/ExamDAO.java) / [`ExamDAOImpl.java`](src/main/java/com/smartstudy/dao/impl/ExamDAOImpl.java) | Database access for exam dates, target marks, and scored marks. |
| [`src/main/java/com/smartstudy/dao/StudySessionDAO.java`](src/main/java/com/smartstudy/dao/StudySessionDAO.java) / [`StudySessionDAOImpl.java`](src/main/java/com/smartstudy/dao/impl/StudySessionDAOImpl.java) | Database access for logged study session hours and topics. |
| [`src/main/java/com/smartstudy/servlet/LoginServlet.java`](src/main/java/com/smartstudy/servlet/LoginServlet.java) | Route `/web-login` — authenticates faculty and initializes HTTP Session. |
| [`src/main/java/com/smartstudy/servlet/DashboardServlet.java`](src/main/java/com/smartstudy/servlet/DashboardServlet.java) | Route `/web-dashboard` — aggregates cohort-wide progress, hours, and task metrics. |
| [`src/main/java/com/smartstudy/servlet/StudentServlet.java`](src/main/java/com/smartstudy/servlet/StudentServlet.java) | Route `/students` — loads enrolled student roster from MySQL. |
| [`src/main/java/com/smartstudy/servlet/ProgressServlet.java`](src/main/java/com/smartstudy/servlet/ProgressServlet.java) | Route `/student-progress` — displays individual student academic profile and tasks. |
| [`src/main/java/com/smartstudy/servlet/ReportServlet.java`](src/main/java/com/smartstudy/servlet/ReportServlet.java) | Route `/web-reports` — lists and previews server-saved weekly TXT reports. |
| [`src/main/webapp/WEB-INF/web.xml`](src/main/webapp/WEB-INF/web.xml) | Jakarta EE web deployment descriptor. |
| [`src/main/webapp/`](src/main/webapp/) | JSP templates (`login.jsp`, `dashboard.jsp`, `students.jsp`, `student-progress.jsp`, `reports.jsp`). |
| [`src/main/webapp/css/web-style.css`](src/main/webapp/css/web-style.css) | Responsive CSS theme for teacher portal. |

---

### 4.3 Detailed Technical Concepts & How They Work in Code

#### A. Database Schema & Relational Design
- **6 Relational Tables** in MySQL:
  1. `users`: Stores ID, Name, Email (UNIQUE), Password, Role (`STUDENT`, `TEACHER`).
  2. `subjects`: Foreign key `student_id -> users(id) ON DELETE CASCADE`.
  3. `tasks`: Foreign key `student_id -> users(id)` and `subject_id -> subjects(id) ON DELETE CASCADE`.
  4. `exams`: Foreign key `subject_id -> subjects(id)`.
  5. `assignments`: Foreign key `subject_id -> subjects(id)`.
  6. `study_sessions`: Foreign key `student_id` and `subject_id`.
- **Integrity**: `ON DELETE CASCADE` ensures that deleting a student cleanly purges their subjects, tasks, and study records without leaving orphaned records.

#### B. JDBC & The Data Access Object (DAO) Pattern
- **Why DAO Pattern?** It separates business logic from low-level SQL database code. The controllers never write SQL queries; they simply call `taskDAO.findByStudentId(id)`.
- **`DBConnection.java`**:
  ```java
  private static final String URL = "jdbc:mysql://localhost:3306/smartstudy?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
  public static Connection getConnection() throws SQLException {
      return DriverManager.getConnection(URL, USER, PASSWORD);
  }
  ```
- **SQL Injection Prevention**: Every query in every DAO uses `PreparedStatement` with parameterized placeholders (`?`). User inputs are never concatenated into raw SQL strings:
  ```java
  String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
  PreparedStatement stmt = conn.prepareStatement(sql);
  stmt.setString(1, email);
  stmt.setString(2, password);
  ```

#### C. Jakarta Servlets 6.0 & JSP 3.1 Web Portal
- **Web MVC Pattern**:
  - **Controller**: Jakarta Servlets (`@WebServlet`) handle incoming HTTP requests (`doGet`, `doPost`), validate parameters, call the Service layer, and attach data to `request.setAttribute(...)`.
  - **View**: JSP pages receive the request via `req.getRequestDispatcher("page.jsp").forward(req, resp)` and render HTML with CSS.
  - **Model**: `User`, `Student`, `Subject`, and `Task` Java objects.
- **Session Management**:
  - In `LoginServlet.java`:
    ```java
    HttpSession session = req.getSession();
    session.setAttribute("user", user);
    session.setAttribute("userName", user.getName());
    ```
  - When the user logs out, `session.invalidate()` is called in `LoginServlet.doGet()`.
- **Live Cohort Aggregations in `DashboardServlet.java`**:
  - Calls `userService.getUsersByRole("STUDENT")`.
  - Iterates through all enrolled students in MySQL and calculates cohort averages:
    - Average Progress %
    - Total Study Hours Logged across all students
    - Sum of active pending tasks vs. completed tasks

---

### 4.4 Evaluator / Teacher Viva Q&A (Deepujjwal)

**Q1: How do you prevent SQL Injection in your database operations?**  
*Answer*: "We strictly use `java.sql.PreparedStatement` with parameterized placeholders (`?`) across all DAO implementations (`UserDAOImpl`, `TaskDAOImpl`, etc.). Input values are sent separately from the SQL statement structure, which means any malicious SQL fragments are treated strictly as literal string values, completely eliminating SQL injection risks."

**Q2: What is the DAO (Data Access Object) pattern and why did you use it?**  
*Answer*: "The DAO pattern is an architectural pattern that abstracts and encapsulates all database interactions behind an interface. Our servlets and services interact only with interfaces like `TaskDAO` and `UserDAO`. This decouples the business logic from MySQL JDBC code, making the codebase easier to test, maintain, or migrate to another database without altering business rules."

**Q3: How does the Teacher Web Portal communicate with the same database as the JavaFX app?**  
*Answer*: "Both the JavaFX desktop app and the Jakarta Servlet web application share the exact same Service layer, DAO layer, and `DBConnection` utility. When a student completes a task or logs study time in the JavaFX client, it executes an `UPDATE` in MySQL. When the teacher refreshes the web portal, `DashboardServlet` reads the updated data directly from MySQL, providing true end-to-end data synchronization."

**Q4: How does session management work in the web portal?**  
*Answer*: "When faculty logs in via `LoginServlet.doPost()`, we create or retrieve the `HttpSession` via `req.getSession()`. We store the authenticated `User` entity and `userName` attribute in session scope so subsequent requests to `/web-dashboard`, `/students`, etc., know who is logged in. When the user clicks 'Logout', `doGet()` in `LoginServlet` invokes `session.invalidate()`, clearing session state."

---

# 🚀 SUMMARY CHECKLIST FOR THE PRESENTATION

1. **Before Evaluation**:
   - Ensure **XAMPP MySQL** is running (green).
   - JavaFX Desktop: Run `.\run.ps1` or run `Launcher.java` in Eclipse.
   - Teacher Web Portal: Tomcat running in Eclipse, open `http://localhost:8080/SmartStudy/login.jsp`.

2. **Presentation Flow**:
   - **Kapish Tickoo** introduces the architecture, OOP class hierarchy (`User`, `Student`, `Teacher`), Strategy pattern for task recommendations, and user-defined exceptions.
   - **Manit** runs the JavaFX desktop app, shows the dashboard KPI cards, clicks **Generate Report** to prove non-blocking multithreading, and demonstrates the custom exception button.
   - **Deepujjwal** opens the web portal, shows live cohort stats calculated from MySQL, navigates to the student roster, inspects an individual student's profile, and opens the generated weekly report in the browser.
