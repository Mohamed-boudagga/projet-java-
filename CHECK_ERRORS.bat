@echo off
setlocal

set JAVA_HOME=%USERPROFILE%\.jdks\ms-21.0.10
set JAVAC="%JAVA_HOME%\bin\javac.exe"
set M2=%USERPROFILE%\.m2\repository
set JFX_V=22.0.2

set LIBS=%M2%\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar;%M2%\org\mindrot\jbcrypt\0.4\jbcrypt-0.4.jar;%M2%\com\sun\mail\javax.mail\1.6.2\javax.mail-1.6.2.jar;%M2%\com\sun\mail\jakarta.mail\2.0.1\jakarta.mail-2.0.1.jar;%M2%\org\openjfx\javafx-base\%JFX_V%\javafx-base-%JFX_V%-win.jar;%M2%\org\openjfx\javafx-controls\%JFX_V%\javafx-controls-%JFX_V%-win.jar;%M2%\org\openjfx\javafx-fxml\%JFX_V%\javafx-fxml-%JFX_V%-win.jar;%M2%\org\openjfx\javafx-graphics\%JFX_V%\javafx-graphics-%JFX_V%-win.jar;%M2%\org\openjfx\javafx-media\%JFX_V%\javafx-media-%JFX_V%-win.jar

if not exist "target\classes" mkdir "target\classes"

dir /s /b "src\main\java\*.java" > sources_list.txt

echo Compilation en cours...
%JAVAC% -encoding UTF-8 -d "target\classes" -cp "%LIBS%" @sources_list.txt 2> full_errors.txt

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo === ERREURS DE COMPILATION ===
    type errors_only.txt
    echo.
    echo Nombre d'erreurs:
    find /c "error:" errors_only.txt
) else (
    echo.
    echo === COMPILATION REUSSIE - Aucune erreur ===
)

del sources_list.txt
