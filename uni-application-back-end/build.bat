@echo off
setlocal enabledelayedexpansion

:: Read the .env file and set environment variables
for /f "tokens=1,2 delims==" %%i in (.env) do (
    set "VAR=%%i"
    set "VALUE=%%j"
    set "!VAR!=!VALUE!"
)

:: Run Maven build
mvn clean install
