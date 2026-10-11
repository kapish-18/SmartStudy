# PowerShell Launcher for SmartStudy Desktop Application
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "   Starting SmartStudy Desktop Application" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

# 1. If Maven is installed, use Maven JavaFX plugin
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "[INFO] Detected Apache Maven. Launching via javafx:run..." -ForegroundColor Green
    mvn compile javafx:run
    exit
}

# 2. Check Java
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] Java is not detected on your PATH! Please install JDK 21+." -ForegroundColor Red
    pause
    exit
}

$m2 = "$env:USERPROFILE\.m2\repository"
$jars = @(
    "$m2\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2.jar",
    "$m2\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2-win.jar",
    "$m2\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2.jar",
    "$m2\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2-win.jar",
    "$m2\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2.jar",
    "$m2\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2-win.jar",
    "$m2\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2.jar",
    "$m2\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2-win.jar",
    "$m2\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar",
    "$m2\jakarta\servlet\jakarta.servlet-api\6.0.0\jakarta.servlet-api-6.0.0.jar",
    "$m2\jakarta\servlet\jsp\jakarta.servlet.jsp-api\3.1.1\jakarta.servlet.jsp-api-3.1.1.jar",
    "target\classes"
) -join ";"

# Compile if target\classes does not exist
if (-not (Test-Path "target\classes\com\smartstudy\Launcher.class")) {
    Write-Host "[INFO] Compiled classes not found. Compiling project..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path "target\classes" -Force | Out-Null
    $sources = (Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java").FullName
    javac -cp $jars -d "target\classes" $sources
    Copy-Item -Path "src\main\resources\*" -Destination "target\classes\" -Recurse -Force
}

Write-Host "[INFO] Launching SmartStudy Desktop App..." -ForegroundColor Green
java -cp $jars com.smartstudy.Launcher
