#!/bin/bash

# 我的赛博冰箱 - 启动脚本 (Linux/Mac)

echo ""
echo "========================================"
echo "   🧊 我的赛博冰箱启动脚本"
echo "========================================"
echo ""

# 检查 Java
if ! command -v java &> /dev/null; then
    echo "❌ 错误：未找到 Java，请先安装 Java 17 或更高版本"
    exit 1
fi

# 检查 Node.js
if ! command -v node &> /dev/null; then
    echo "❌ 错误：未找到 Node.js，请先安装 Node.js 16 或更高版本"
    exit 1
fi

echo "✅ 检查通过：Java 和 Node.js 已安装"
echo ""

# 启动后端
echo "🚀 启动后端服务器 (Spring Boot)..."
echo "访问地址: http://localhost:8080"
echo ""

cd backend
mvn spring-boot:run &
BACKEND_PID=$!

# 等待后端启动
sleep 5

# 启动前端
echo ""
echo "🚀 启动前端开发服务器 (Vue 3 + Vite)..."
echo "访问地址: http://localhost:5173"
echo ""

cd ../frontend
npm install --legacy-peer-deps > /dev/null 2>&1
npm run dev &
FRONTEND_PID=$!

# 捕获 Ctrl+C 信号
trap "kill $BACKEND_PID $FRONTEND_PID; exit 0" SIGINT

# 等待进程
wait
