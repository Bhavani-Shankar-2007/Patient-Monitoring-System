@echo off
echo =============================================
echo     SpiderAdhere - Patient Monitoring App
echo =============================================
echo.

if not exist bin mkdir bin

echo Compiling...
javac -cp "lib\gson-2.11.0.jar;src\main\java" -d bin src\main\java\com\sns\hackathon\*.java

if %errorlevel% neq 0 (
    echo.
    echo ❌ Compilation failed! Check the errors above.
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Starting SpiderAdhere...
echo Open your browser and go to: http://localhost:8080
echo Press Ctrl+C to stop the server.
echo.

java -cp "bin;lib\gson-2.11.0.jar" com.sns.hackathon.PatientMonitorApplication

pause