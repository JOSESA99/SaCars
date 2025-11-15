# Backend SaCars - instrucciones rápidas (Windows)

Requisitos:
- JDK instalado (javac en PATH). Comprobar: `javac -version`
- Coloca `mysql-connector-java-8.0.33.jar` en `backend\lib\`

PowerShell (recomendado):
1. Abrir PowerShell en `c:\xampp\htdocs\SaCars\backend`
2. (Si bloquea scripts) ejecutar:
   Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass -Force
3. Ejecutar:
   .\build_windows.ps1
4. Ejecutar servidor:
   java -cp build;lib\mysql-connector-java-8.0.33.jar com.sacars.Main

CMD (alternativa):
1. Abrir cmd.exe en `c:\xampp\htdocs\SaCars\backend`
2. Ejecutar:
   build_and_run.bat

Comandos manuales (si prefieres):
- Generar lista de fuentes:
  PowerShell: Get-ChildItem -Recurse -Filter *.java | % FullName > sources.txt
  CMD: for /r %%f in (*.java) do @echo %%~ff >> sources.txt
- Compilar:
  javac -cp lib\mysql-connector-java-8.0.33.jar -d build @sources.txt
- Ejecutar:
  java -cp build;lib\mysql-connector-java-8.0.33.jar com.sacars.Main

Errores comunes:
- "javac no se reconoce": instala JDK y agrega la carpeta bin al PATH.
- "No se encontró mysql-connector...": descarga el connector y colócalo en backend\lib\.
- Errores de compilación: pega aquí la salida completa de javac y te indico correcciones.

Notas:
- En Windows el separador de classpath es `;`.
- Asegúrate que la base de datos `sacars_db` existe y credenciales en Database.java son correctas.
