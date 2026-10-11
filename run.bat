@echo off
setlocal enabledelayedexpansion

echo ========================================================
echo   Starting SmartStudy Desktop Application
echo ========================================================

:: 1. If Maven is installed, use Maven JavaFX plugin (standard way)
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo [INFO] Detected Apache Maven on system PATH.
    echo [INFO] Launching via Maven JavaFX plugin...
    mvn compile javafx:run
    goto :end
)

:: 2. Otherwise, check for Java and compile/run via classpath using user profile
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java is not detected on your PATH!
    echo Please install JDK 21+ and add it to your PATH environment variable.
    pause
    exit /b 1
)

set "M2_REPO=%USERPROFILE%\.m2\repository"
set "CP=target\classes;%M2_REPO%\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2.jar;%M2_REPO%\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2-win.jar;%M2_REPO%\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2.jar;%M2_REPO%\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2-win.jar;%M2_REPO%\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2.jar;%M2_REPO%\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2-win.jar;%M2_REPO%\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2.jar;%M2_REPO%\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2-win.jar;%M2_REPO%\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar;%M2_REPO%\jakarta\servlet\jakarta.servlet-api\6.0.0\jakarta.servlet-api-6.0.0.jar;%M2_REPO%\jakarta\servlet\jsp\jakarta.servlet.jsp-api\3.1.1\jakarta.servlet.jsp-api-3.1.1.jar"

:: Check if compiled classes exist
if not exist "target\classes\com\smartstudy\Launcher.class" (
    echo [INFO] Compiled classes not found. Compiling project...
    if not exist "target\classes" mkdir "target\classes"
    
    dir /s /b src\main\java\*.java > sources.txt
    javac -cp "%CP%" -d target\classes @sources.txt
    del sources.txt
    
    xcopy /E /I /Y src\main\resources target\classes >nul 2>&1
)

echo [INFO] Launching application...
java -cp "%CP%" com.smartstudy.Launcher

:end
pause
