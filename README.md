# 🧊 我的赛博冰箱

一个可爱、互动的个人主页留言网站。来吧，在我的冰箱上贴张便签吧！

## ✨ 功能特点

- 🎨 **可爱的UI设计** - 奶油手账风 + 轻微赛博感
- 📝 **便签留言** - 支持多色便利贴，可拖拽移动
- 🧲 **加磁铁** - 点赞功能，显示便签热度
- 📊 **今日状态** - 展示精神/社交/创作电量和状态
- ❄️ **冰箱对话** - 根据状态随机显示可爱文案
- 💾 **本地存储** - 便签和状态自动保存
- 🎭 **丰富交互** - 拖拽、动画、悬停效果

## 🛠️ 技术栈

### 后端
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database (内存数据库)
- Java 17

### 前端
- Vue 3
- Vite
- Axios
- CSS3

## 📦 项目结构

```
MyFridge/
├── backend/                    # 后端 Spring Boot 项目
│   ├── pom.xml                # Maven 配置
│   ├── src/
│   │   ├── main/java/com/myfridge/
│   │   │   ├── MyFridgeApplication.java    # 主应用类
│   │   │   ├── controller/    # REST 控制器
│   │   │   ├── service/       # 业务逻辑
│   │   │   ├── entity/        # 数据实体
│   │   │   └── repository/    # 数据库访问
│   │   └── resources/
│   │       └── application.yml # Spring Boot 配置
│
├── frontend/                   # 前端 Vue 3 项目
│   ├── package.json           # NPM 依赖
│   ├── vite.config.js         # Vite 配置
│   ├── index.html             # HTML 入口
│   ├── src/
│   │   ├── main.js            # 应用入口
│   │   ├── App.vue            # 根组件
│   │   ├── style.css          # 全局样式
│   │   ├── components/        # Vue 组件
│   │   │   ├── Fridge.vue     # 冰箱主体
│   │   │   ├── Note.vue       # 便签组件
│   │   │   ├── NoteModal.vue  # 新建便签弹窗
│   │   │   └── StatusModal.vue # 编辑状态弹窗
│   │   ├── services/
│   │   │   └── api.js         # API 接口
│   │   └── utils/
│   │       └── helpers.js     # 工具函数
│   └── public/                # 静态资源
│
└── README.md                   # 项目说明
```

## 🚀 快速开始

### 前置要求
- Java 17+
- Node.js 16+
- Maven 3.6+

### 后端启动

1. 进入后端目录
```bash
cd backend
```

2. 构建项目
```bash
mvn clean install
```

3. 启动应用
```bash
mvn spring-boot:run
```

应用会在 `http://localhost:8080` 启动

### 前端启动

1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run dev
```

访问 `http://localhost:5173` 即可使用

## 📝 使用说明

### 创建便签

1. 点击右下角的 **✏️ 贴一张便签** 按钮
2. 填写内容（最多200字）
3. 可选择：
   - 昵称（不填会自动生成）
   - 便签颜色（6种颜色可选）
   - 贴纸图标（10个emoji可选）
   - 保鲜期（暂时不生效）
4. 点击 **贴上去** 提交

### 交互操作

- **鼠标悬停便签**：便签会轻轻抬起，显示操作按钮
- **拖拽便签**：点击并拖动便签到不同位置，位置自动保存
- **加磁铁**：点击便签上的 🧲 按钮，磁铁数 +1
- **删除便签**：点击便签上的 ✕ 按钮

### 编辑冰箱状态

1. 点击冰箱门上的 ✏️ 按钮
2. 调整精神/社交/创作电量（0-100%）
3. 输入今日饮品、关键词、备注
4. 点击保存

### 冰箱对话

冰箱会根据以下情况随机显示文案：
- 没有便签时：显示空状态文案
- 有新便签时：显示欢迎文案
- 便签很多时：显示满了文案
- 主人精神电量低时：显示关心文案
- 其他时间：显示随机可爱文案

## 💾 数据存储

### 后端
- 使用 H2 内存数据库存储数据
- 应用重启后数据会丢失
- 可修改 `application.yml` 改用 MySQL 等持久化数据库

### 前端
- 便签位置和冰箱状态自动保存到浏览器 localStorage
- 刷新页面后数据依然存在

## 🎨 自定义样式

### 便签颜色
编辑 `frontend/src/utils/helpers.js` 中的 `noteColors` 对象：
```javascript
export const noteColors = [
  { name: 'cream', label: '奶油黄', bg: '#fffacd', text: '#8b8b00' },
  // ...
]
```

### 背景颜色
编辑 `frontend/src/style.css` 中的 `body` 样式：
```css
body {
  background: linear-gradient(135deg, #fef5e7 0%, #fdeaea 50%, #eaf8f5 100%);
}
```

## 📱 响应式设计

项目在以下设备上进行了优化：
- 💻 桌面端 (1920px+)
- 📱 平板 (768px)
- 📱 手机 (320px+)

## 🐛 常见问题

### Q: 后端启动失败
A: 确保：
- Java 版本 >= 17
- 端口 8080 未被占用
- Maven 已正确安装

### Q: 前端无法连接到后端
A: 检查：
- 后端是否正在运行 (http://localhost:8080)
- CORS 配置是否正确
- Vite 代理配置是否生效

### Q: 数据重启后丢失
A: 这是正常行为。如需持久化存储，请将 H2 数据库改为 MySQL：
1. 修改 `pom.xml` 加入 MySQL 驱动
2. 修改 `application.yml` 配置数据库连接

## 🚀 部署

### Docker 部署

创建 Dockerfile（可选）：

```dockerfile
# 后端
FROM maven:3.8-openjdk-17 AS backend-build
WORKDIR /app
COPY backend/pom.xml .
RUN mvn dependency:go-offline
COPY backend/src ./src
RUN mvn clean package

# 前端
FROM node:18 AS frontend-build
WORKDIR /app
COPY frontend/package.json .
RUN npm install
COPY frontend . 
RUN npm run build

# 最终镜像
FROM openjdk:17-slim
COPY --from=backend-build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 生产环境配置

修改 `application.yml` for 生产环境：
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  datasource:
    url: jdbc:mysql://your-db-host:3306/myfridge
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```

## 📄 许可证

MIT License

## 👋 贡献

欢迎提交 Issue 和 Pull Request！

## 💌 特别感谢

感谢所有来访者在冰箱门上贴下的每一张便签。

---

**来都来了，贴张便签再走吧。** ❄️✨
