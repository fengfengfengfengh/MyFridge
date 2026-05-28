如何快速启动项目？

## Windows 用户

### 方法1：双击启动脚本（推荐）
1. 在项目根目录找到 `start.bat` 文件
2. 双击运行
3. 等待两个命令行窗口启动
4. 打开浏览器访问 http://localhost:5173

### 方法2：手动启动

**终端1 - 启动后端：**
```bash
cd backend
mvn spring-boot:run
```

**终端2 - 启动前端：**
```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:5173

---

## Mac / Linux 用户

### 方法1：运行启动脚本（推荐）
```bash
chmod +x start.sh
./start.sh
```

### 方法2：手动启动

**终端1 - 启动后端：**
```bash
cd backend
mvn spring-boot:run
```

**终端2 - 启动前端：**
```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:5173

---

## 环境要求检查

运行以下命令检查环境：

```bash
# 检查 Java（需要 17 或更高）
java -version

# 检查 Node.js（需要 16 或更高）
node --version

# 检查 npm
npm --version

# 检查 Maven
mvn --version
```

---

## 首次启动步骤

1. **确保后端完全启动**
   - 看到 "Tomcat started on port(s): 8080" 消息

2. **前端会自动加载数据**
   - 页面应该显示空的冰箱门和 "冰箱门还空着" 提示

3. **点击 "贴一张便签" 创建第一张便签**
   - 便签会出现在冰箱门上
   - 点击 ✏️ 可以编辑今日状态

---

## 常见问题快速解决

### Q: 提示找不到 Java
**A:** 
- Windows: 安装 JDK 17，重启电脑
- Mac: `brew install openjdk@17` 后配置 PATH
- Linux: `sudo apt install openjdk-17-jdk`

### Q: 提示找不到 Node.js
**A:**
- 访问 https://nodejs.org 下载安装 LTS 版本

### Q: 后端 8080 端口被占用
**A:** 
修改 `backend/src/main/resources/application.yml`：
```yaml
server:
  port: 8081  # 改成其他端口
```

### Q: 前端无法连接后端（404 或超时）
**A:**
- 确保后端已启动在 http://localhost:8080
- 检查网络连接
- F12 打开浏览器开发者工具查看错误

### Q: 页面加载但什么都看不到
**A:**
- 等待 5-10 秒让页面完全加载
- F12 查看控制台是否有错误
- 尝试 Ctrl+Shift+R 强制刷新

---

## 功能测试检查表

启动成功后，请测试以下功能：

- [ ] 页面显示冰箱和顶部标题
- [ ] 点击 ✏️ 按钮能打开新建便签弹窗
- [ ] 输入内容、选择颜色、选择贴纸
- [ ] 点击 "贴上去" 后便签出现在冰箱上
- [ ] 鼠标悬停便签显示操作按钮
- [ ] 可以拖拽便签移动位置
- [ ] 点击 🧲 按钮磁铁数增加
- [ ] 点击 ✕ 删除便签
- [ ] 点击状态卡的 ✏️ 能编辑今日状态
- [ ] 冰箱门下方显示随机文案

全部通过 ✅ 表示启动成功！

---

## 停止服务

### Windows
直接关闭两个命令行窗口

### Mac / Linux
在启动脚本的终端中按 Ctrl+C

---

## 下一步

- 查看 README.md 了解详细功能
- 查看 DEVELOPMENT.md 了解开发细节
- 打开浏览器开发者工具 (F12) 调试前端
- 访问 http://localhost:8080/h2-console 查看数据库

---

**需要帮助？查看 README.md 中的"常见问题"部分。** 💌
