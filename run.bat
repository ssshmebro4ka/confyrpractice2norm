@echo off
chcp 65001 >nul
setlocal

set OUT_DIR=out
set MAIN_CLASS=Main
set CONFIG=%1
if "%CONFIG%"=="" set CONFIG=config.json

echo Компиляция...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

:: Переходим в папку src, чтобы избавиться от пробелов в пути
cd src

:: Компилируем файлы. Здесь пути относительные, без пробелов.
javac -encoding UTF-8 -d "..\%OUT_DIR%" *.java

:: Если компиляция не удалась - выходим
if errorlevel 1 (
    echo Ошибка компиляции.
    cd ..
    pause
    exit /b 1
)

:: Возвращаемся в корень проекта
cd ..

echo Запуск...
:: Запускаем скомпилированный класс из папки out
java -cp "%OUT_DIR%" %MAIN_CLASS% %CONFIG%

pause
endlocal