@echo off
REM 我的赛博冰箱 - 启动脚本 (Windows)

echo.
echo ========================================
echo    🧊 我的赛博冰箱启动脚本
echo ========================================
echo.

REM 检查 Java 是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ 错误：未找到 Java，请先安装 Java 17 或更高版本
    pause
    exit /b 1
)

REM 检查 Node.js 是否安装
node --version >nul 2>&1
if errorlevel 1 (
    echo ❌ 错误：未找到 Node.js，请先安装 Node.js 16 或更高版本
    pause
    exit /b 1
)

echo ✅ 检查通过：Java 和 Node.js 已安装
echo.

REM 启动后端
echo 🚀 启动后端服务器 (Spring Boot)...
echo 访问地址: http://localhost:8080
cd backend
start cmd /k "mvn spring-boot:run"
timeout /t 5

REM 启动前端
echo.
echo 🚀 启动前端开发服务器 (Vue 3 + Vite)...
echo 访问地址: http://localhost:5173
cd ..\frontend
call npm install --legacy-peer-deps >nul 2>&1
npm run dev

pause
