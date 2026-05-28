# 📖 开发指南

## 项目架构

### 系统架构图

```
┌─────────────────────────────────────────────────────────┐
│                   浏览器 (Frontend)                      │
│  Vue 3 + Vite + Axios                                  │
│  - App.vue (主容器)                                    │
│  - Fridge.vue (冰箱主体)                               │
│  - Note.vue (便签组件)                                 │
│  - NoteModal.vue (新建便签)                            │
│  - StatusModal.vue (编辑状态)                          │
└──────────────────┬──────────────────────────────────────┘
                   │ HTTP/REST API
                   │ :5173 <-> :8080
┌──────────────────▼──────────────────────────────────────┐
│                服务器 (Backend)                         │
│  Spring Boot 3.1.5                                     │
│  ┌──────────────────────────────────────────────────┐  │
│  │ Controller (REST 接口)                           │  │
│  │ - NoteController (/api/notes)                    │  │
│  │ - FridgeStatusController (/api/fridge-status)   │  │
│  └──────────────────┬───────────────────────────────┘  │
│                     │                                   │
│  ┌──────────────────▼───────────────────────────────┐  │
│  │ Service (业务逻辑)                               │  │
│  │ - NoteService                                    │  │
│  │ - FridgeStatusService                            │  │
│  └──────────────────┬───────────────────────────────┘  │
│                     │                                   │
│  ┌──────────────────▼───────────────────────────────┐  │
│  │ Repository (数据持久化)                          │  │
│  │ - NoteRepository extends JpaRepository           │  │
│  │ - FridgeStatusRepository extends JpaRepository   │  │
│  └──────────────────┬───────────────────────────────┘  │
│                     │                                   │
│  ┌──────────────────▼───────────────────────────────┐  │
│  │ Entity (数据模型)                                │  │
│  │ - Note (便签)                                    │  │
│  │ - FridgeStatus (冰箱状态)                        │  │
│  └──────────────────┬───────────────────────────────┘  │
│                     │                                   │
└─────────────────────┼──────────────────────────────────┘
                      │ JDBC
┌─────────────────────▼──────────────────────────────────┐
│              H2 Database (内存数据库)                   │
│  - notes (便签表)                                     │
│  - fridge_status (冰箱状态表)                         │
└─────────────────────────────────────────────────────────┘
```

## API 文档

### 便签相关

#### GET /api/notes
获取所有活跃的便签

**响应示例:**
```json
[
  {
    "id": 1,
    "content": "你好呀！",
    "nickname": "路过的电子企鹅",
    "color": "pink",
    "icon": "💌",
    "magnetCount": 5,
    "positionX": 12.5,
    "positionY": 20.3,
    "createdAt": "2024-05-28T10:30:00",
    "isActive": true
  }
]
```

#### POST /api/notes
创建新便签

**请求示例:**
```json
{
  "content": "贴一张新便签",
  "nickname": "冰箱贴客",
  "color": "cream",
  "icon": "🍅"
}
```

**响应示例:**
```json
{
  "success": true,
  "message": "贴好了。冰箱轻轻嗡了一声，好像收到了。",
  "data": { ... }
}
```

#### GET /api/notes/{id}
获取单个便签

#### POST /api/notes/{id}/magnet
增加便签磁铁数

**响应示例:**
```json
{
  "success": true,
  "magnetCount": 6
}
```

#### PUT /api/notes/{id}/position
更新便签位置

**查询参数:**
- `x`: 横坐标百分比 (0-100)
- `y`: 纵坐标百分比 (0-100)

#### DELETE /api/notes/{id}
删除便签（软删除）

### 冰箱状态相关

#### GET /api/fridge-status
获取冰箱状态

**响应示例:**
```json
{
  "id": 1,
  "energyLevel": 62,
  "socialLevel": 24,
  "creativityLevel": 88,
  "todayDrink": "冰美式",
  "todayKeyword": "缓慢重启",
  "todayNote": "不要问，问就是正在加载。",
  "updatedAt": "2024-05-28T10:30:00"
}
```

#### PUT /api/fridge-status
更新冰箱状态

**请求示例:**
```json
{
  "energyLevel": 70,
  "socialLevel": 50,
  "creativityLevel": 85,
  "todayDrink": "热奶茶",
  "todayKeyword": "摸鱼",
  "todayNote": "今天状态还不错"
}
```

## 前端开发

### 组件通讯

```
App.vue
├── 状态管理
│   ├── notes (便签列表)
│   ├── fridgeStatus (冰箱状态)
│   └── modals (弹窗显示状态)
│
├── Fridge.vue
│   ├── 显示冰箱主体
│   ├── 显示今日状态卡
│   ├── 显示便签列表
│   └── 发送事件到 App
│
├── Note.vue (v-for 循环)
│   ├── 显示单个便签
│   ├── 拖拽功能
│   ├── 磁铁按钮
│   └── 删除按钮
│
├── NoteModal.vue
│   ├── 输入便签内容
│   ├── 选择颜色/图标
│   └── 提交新便签
│
└── StatusModal.vue
    ├── 调整能量条
    ├── 输入状态信息
    └── 保存状态
```

### localStorage 数据结构

```javascript
// 便签列表缓存
localStorage.setItem('notes-cache', JSON.stringify(notes))

// 便签位置
localStorage.setItem('note-pos-{id}', JSON.stringify({ x, y }))

// 冰箱状态
localStorage.setItem('fridge-status', JSON.stringify(status))
```

### 样式系统

#### 颜色变量
- 主色：#ffcc99 (奶油色)
- 强调色：#ff9999 (粉红)
- 背景：线性渐变 (奶油色+粉红+薄荷)
- 文字：#5a5a5a (深灰)

#### 圆角
- 大组件：20-24px
- 中组件：12-16px
- 小组件：6-8px

#### 阴影
- 浅阴影：0 4px 12px rgba(0,0,0,0.08)
- 中阴影：0 8px 20px rgba(0,0,0,0.15)
- 深阴影：0 12px 30px rgba(0,0,0,0.2)

## 后端开发

### 数据库表设计

#### notes 表
```sql
CREATE TABLE notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT,
    nickname VARCHAR(50),
    color VARCHAR(20),
    icon VARCHAR(10),
    magnet_count INT DEFAULT 0,
    position_x DOUBLE,
    position_y DOUBLE,
    created_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);
```

#### fridge_status 表
```sql
CREATE TABLE fridge_status (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    energy_level INT,
    social_level INT,
    creativity_level INT,
    today_drink VARCHAR(50),
    today_keyword VARCHAR(50),
    today_note TEXT,
    updated_at TIMESTAMP
);
```

### 常见业务逻辑

#### 软删除
```java
// 删除不实际删除，只标记 is_active = false
noteRepository.findByIsActiveTrueOrderByCreatedAtDesc();
```

#### 位置保存
```java
// 位置通过 Double 类型百分比存储
// 前端: 0-100% 的视口位置
// 后端: Double 类型 (0-100)
```

## 调试技巧

### 前端调试

1. **Vue DevTools**
   - 安装 Vue.js devtools 浏览器扩展
   - 检查组件状态
   - 追踪事件流

2. **浏览器控制台**
   ```javascript
   // 查看所有便签
   JSON.parse(localStorage.getItem('notes-cache'))
   
   // 查看冰箱状态
   JSON.parse(localStorage.getItem('fridge-status'))
   
   // 清除所有数据
   localStorage.clear()
   ```

3. **网络请求**
   - 打开 F12 -> Network
   - 查看 API 请求和响应
   - 检查 CORS 错误

### 后端调试

1. **日志**
   ```yaml
   # application.yml
   logging:
     level:
       com.myfridge: DEBUG
   ```

2. **H2 控制台**
   - 访问 http://localhost:8080/h2-console
   - 查询和修改数据库

3. **Postman/Insomnia**
   - 测试 API 端点
   - 检查请求/响应格式

## 性能优化

### 前端优化
- 使用 v-show 而不是 v-if 为频繁切换的组件
- 便签列表使用虚拟滚动（大量便签时）
- 使用 requestAnimationFrame 优化拖拽动画

### 后端优化
- 分页查询大量便签 (`@GetMapping("?page=0&size=20")`)
- 添加数据库索引 (`@Index(name = "idx_is_active", columnList = "is_active")`)
- 使用查询缓存

## 常见问题解决

### CORS 错误
前端默认配置了代理，但如果直接调用后端 URL，需要确保 CORS 配置：

```java
// MyFridgeApplication.java 中已配置
registry.addMapping("/**")
    .allowedOrigins("*")
    .allowedMethods("*");
```

### 位置偏移问题
确保计算位置时使用的是容器相对位置，而不是视口位置：

```javascript
// ❌ 错误：相对于视口
const x = e.clientX

// ✅ 正确：相对于容器
const containerRect = container.getBoundingClientRect()
const x = e.clientX - containerRect.left
```

## 扩展建议

1. **用户系统**
   - 添加登录/注册
   - 多用户支持
   - 权限管理

2. **数据持久化**
   - 使用 MySQL 代替 H2
   - 定期备份
   - 数据导出功能

3. **高级功能**
   - 便签搜索/筛选
   - 评论功能
   - 表情包库
   - 主题切换

4. **社交功能**
   - 便签分享
   - 随机访问他人冰箱
   - 便签排行榜

---

**祝开发愉快！** ✨
