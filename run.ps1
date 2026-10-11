# PowerShell Launcher for SmartStudy Desktop Application
$jars = @(
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2.jar",
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2-win.jar",
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2.jar",
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2-win.jar",
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2.jar",
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2-win.jar",
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2.jar",
    "C:\Users\Kapish\.m2\repository\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2-win.jar",
    "C:\Users\Kapish\.m2\repository\jakarta\servlet\jakarta.servlet-api\6.0.0\jakarta.servlet-api-6.0.0.jar",
    "C:\Users\Kapish\.m2\repository\jakarta\servlet\jsp\jakarta.servlet.jsp-api\3.1.1\jakarta.servlet.jsp-api-3.1.1.jar",
    "C:\Users\Kapish\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar",
    "target\classes"
) -join ";"

Write-Host "Launching SmartStudy Desktop Application..." -ForegroundColor Cyan
java -cp $jars com.smartstudy.Launcher
