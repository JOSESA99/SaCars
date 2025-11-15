```powershell
# Script PowerShell para compilar el backend en Windows
# Uso: abrir PowerShell en c:\xampp\htdocs\SaCars\backend y ejecutar: .\build_windows.ps1

if (-not (Get-Command "javac" -ErrorAction SilentlyContinue)) {
    Write-Error "javac no está en el PATH. Instala JDK y agrega javac al PATH."
    exit 1
}

$sourcesFile = Join-Path -Path (Get-Location) -ChildPath "sources.txt"
if (Test-Path $sourcesFile) { Remove-Item $sourcesFile -Force }

Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Set-Content -Encoding ASCII $sourcesFile
Write-Host "Lista de fuentes generada en backend\sources.txt"

$driver = ".\lib\mysql-connector-java-8.0.33.jar"
if (-not (Test-Path $driver)) {
    Write-Warning "No se encontró $driver. Coloca el connector MySQL en backend\lib\ o actualiza la variable `$driver."
}

if (-Not (Test-Path .\build)) {
    New-Item -ItemType Directory -Path .\build | Out-Null
}

$argList = @("-cp", $driver, "-d", ".\build", "@$sourcesFile")
Write-Host "Ejecutando: javac $($argList -join ' ')"
$proc = Start-Process -FilePath "javac" -ArgumentList $argList -NoNewWindow -Wait -PassThru

if ($proc.ExitCode -eq 0) {
    Write-Host "Compilación exitosa." -ForegroundColor Green
    Write-Host "Para ejecutar: java -cp build;lib\mysql-connector-java-8.0.33.jar com.sacars.Main"
    exit 0
} else {
    Write-Error "Error en la compilación. Código de salida: $($proc.ExitCode)."
    exit $proc.ExitCode
}
```