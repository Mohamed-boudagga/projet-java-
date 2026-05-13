@echo off
setlocal
REM Désactivation de delayedexpansion pour éviter les conflits avec !
title SkillQuest - Battle Arena
echo =====================================================
echo   SKILLQUEST BATTLE ARENA - Demarrage
echo =====================================================

REM --- CONFIGURATION ---
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_301
set JAVAC="%JAVA_HOME%\bin\javac.exe"
set JAVAXE="%JAVA_HOME%\bin\java.exe"
set MYSQL_JAR=%USERPROFILE%\.m2\repository\mysql\mysql-connector-java\5.1.47\mysql-connector-java-5.1.47.jar
set MAIL_API=%USERPROFILE%\.m2\repository\javax\mail\javax.mail-api\1.6.2\javax.mail-api-1.6.2.jar
set MAIL_IMPL=%USERPROFILE%\.m2\repository\com\sun\mail\javax.mail\1.6.2\javax.mail-1.6.2.jar
set LIBS=%MYSQL_JAR%;%MAIL_API%;%MAIL_IMPL%

cd /d "%~dp0"

REM --- COMPILATION ---
echo [1/3] Compilation...
if not exist "target\classes" mkdir "target\classes"
dir /s /b "src\main\java\*.java" > sources_list.txt
%JAVAC% -encoding UTF-8 -source 1.8 -target 1.8 -d "target\classes" -cp "%LIBS%" @sources_list.txt
del sources_list.txt

if %ERRORLEVEL% NEQ 0 (
    echo ERREUR DE COMPILATION !
    pause
    exit /b 1
)

REM --- RESSOURCES ---
echo [2/3] Copie des ressources...
xcopy /E /Y /I /Q "src\main\resources" "target\classes" >nul 2>nul

REM --- LANCEMENT ---
echo [3/3] Lancement...
set CLASSPATH=target\classes;%LIBS%
%JAVAXE% -cp "%CLASSPATH%" main.AppLauncher

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR: Verifiez que MySQL est demarre et que les JARs sont presents.
    pause
)
