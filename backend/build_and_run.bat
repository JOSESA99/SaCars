@echo off
REM Script para compilar y ejecutar desde CMD (Windows)
REM Uso: abrir CMD en c:\xampp\htdocs\SaCars\backend y ejecutar: build_and_run.bat

REM Comprobar javac en PATH
where javac >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: javac no encontrado en PATH. Instala JDK y agrega la carpeta bin al PATH.
    pause
    exit /b 1
)

REM Eliminar sources.txt previo si existe
if exist sources.txt del /f /q sources.txt

REM Generar lista de fuentes Java recursivamente (rutas absolutas)
for /r %%f in (*.java) do (
    echo %%~ff>>sources.txt
)
echo Lista de fuentes generada en backend\sources.txt
echo (Tamaño: %~z1 bytes) 2>nul

REM Verificar driver MySQL
set DRIVER=lib\mysql-connector-java-8.0.33.jar
if not exist "%DRIVER%" (
    echo Aviso: no se encontro %DRIVER%. Por favor descarga mysql-connector y colocalo en backend\lib\
    pause
)

REM Crear carpeta build si no existe
if not exist build (
    mkdir build
)

REM Compilar
echo Compilando...
javac -cp "%DRIVER%" -d build @sources.txt
if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: la compilacion fallo. Revisa los mensajes anteriores de javac.
    echo Copia toda la salida y pegame aqui para ayudarte.
    pause
    exit /b 1
)

echo Compilacion exitosa.
echo Iniciando servidor...
java -cp build;%DRIVER% com.sacars.Main
pause
