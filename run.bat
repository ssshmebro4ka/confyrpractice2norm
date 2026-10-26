@echo off
chcp 65001 >nul
setlocal

set OUT_DIR=out
set MAIN_CLASS=Main
set CONFIG=%1
if "%CONFIG%"=="" set CONFIG=config.json

echo Compiling...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

:: Переходим в папку src
cd src

:: Компилируем файлы
javac -encoding UTF-8 -d "..\%OUT_DIR%" *.java

if errorlevel 1 (
    echo Compilation error.
    cd ..
    pause
    exit /b 1
)

cd ..

echo Running...
java -cp "%OUT_DIR%" %MAIN_CLASS% %CONFIG%

pause
endlocal