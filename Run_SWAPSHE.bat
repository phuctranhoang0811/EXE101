@echo off
title SWAPSHE Emulator & App Runner
echo ========================================================
echo       SWAPSHE - Loved Before, Styled Again
echo       Dang khoi chay May ao Pixel 10 Pro XL...
echo ========================================================

set SDK_DIR=%LOCALAPPDATA%\Android\Sdk
set EMU_EXE=%SDK_DIR%\emulator\emulator.exe
set ADB_EXE=%SDK_DIR%\platform-tools\adb.exe
set APK_PATH=%~dp0app\build\outputs\apk\debug\app-debug.apk

REM Khoi dong emulator
start "" "%EMU_EXE%" -avd Pixel_10_Pro_XL

echo [1/3] May ao dang khoi dong tren man hinh cua ban...
echo Dang cho Android khoi dong hoan tat...

"%ADB_EXE%" wait-for-device

:WAIT_BOOT
for /f "tokens=*" %%a in ('"%ADB_EXE%" shell getprop sys.boot_completed 2^>nul') do set BOOT=%%a
if "%BOOT%"=="1" goto BOOT_DONE
timeout /t 2 /nobreak >nul
goto WAIT_BOOT

:BOOT_DONE
echo [2/3] May ao da san sang! Dang cai dat app SWAPSHE...
"%ADB_EXE%" install -r "%APK_PATH%"

echo [3/3] Dang mo app SWAPSHE tren may ao...
"%ADB_EXE%" shell am start -n com.swapshe.app/.MainActivity

echo.
echo ========================================================
echo   SWAPSHE da mo thanh cong tren may ao!
echo   Ban co the tu do click trai nghiem va review UI.
echo ========================================================
pause
