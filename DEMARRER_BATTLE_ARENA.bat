@echo off
setlocal
title SkillQuest - Battle Arena (Integrated)
echo =====================================================
echo   SKILLQUEST BATTLE ARENA - Demarrage (INTEGRATION)
echo =====================================================

REM --- CONFIGURATION JAVA ---
set JAVA_HOME=%USERPROFILE%\.jdks\ms-21.0.10
set JAVAC="%JAVA_HOME%\bin\javac.exe"
set JAVAXE="%JAVA_HOME%\bin\java.exe"

REM --- DEPENDANCES ---
set M2=%USERPROFILE%\.m2\repository
set JFX_V=22.0.2

REM On ne met PAS de guillemets ici
set MYSQL=%M2%\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar
set JBCRYPT=%M2%\org\mindrot\jbcrypt\0.4\jbcrypt-0.4.jar
set MAIL_JX=%M2%\com\sun\mail\javax.mail\1.6.2\javax.mail-1.6.2.jar
set MAIL_JK=%M2%\com\sun\mail\jakarta.mail\2.0.1\jakarta.mail-2.0.1.jar
set ACTIVATION=%M2%\com\sun\activation\jakarta.activation\2.0.1\jakarta.activation-2.0.1.jar

REM JAVAFX
set JFX_BASE=%M2%\org\openjfx\javafx-base\%JFX_V%\javafx-base-%JFX_V%-win.jar
set JFX_CTRL=%M2%\org\openjfx\javafx-controls\%JFX_V%\javafx-controls-%JFX_V%-win.jar
set JFX_FXML=%M2%\org\openjfx\javafx-fxml\%JFX_V%\javafx-fxml-%JFX_V%-win.jar
set JFX_GRPH=%M2%\org\openjfx\javafx-graphics\%JFX_V%\javafx-graphics-%JFX_V%-win.jar
set JFX_MEDIA=%M2%\org\openjfx\javafx-media\%JFX_V%\javafx-media-%JFX_V%-win.jar

set LIBS=%MYSQL%;%JBCRYPT%;%MAIL_JX%;%MAIL_JK%;%ACTIVATION%;%JFX_BASE%;%JFX_CTRL%;%JFX_FXML%;%JFX_GRPH%;%JFX_MEDIA%

cd /d "%~dp0"

REM --- COMPILATION ---
echo [1/3] Compilation...
if not exist "target\classes" mkdir "target\classes"
dir /s /b "src\main\java\*.java" > sources_list.txt
REM On met les guillemets autour de toute la variable LIBS ici
%JAVAC% -encoding UTF-8 -d "target\classes" -cp "%LIBS%" @sources_list.txt
del sources_list.txt

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR DE COMPILATION !
    pause
    exit /b 1
)

REM --- RESSOURCES ---
echo [2/3] Copie des ressources...
xcopy /E /Y /I /Q "src\main\resources" "target\classes" >nul 2>nul

REM --- LANCEMENT ---
echo [3/3] Lancement de l'application...
%JAVAXE% -cp "target\classes;%LIBS%" main.MainLauncher

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR: L'application s'est arretee avec une erreur.
    pause
)
