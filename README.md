# 高校实习管理系统

一个基于 Spring Boot + Vue3 的实习管理平台，支持学生、教师、企业和管理员四种角色，实现实习全流程管理。

## 功能特性

### 用户角色

| 角色 | 功能权限 |
|------|----------|
| 学生 | 查看岗位、申请实习、提交周报、提交实习报告、查看成绩 |
| 教师 | 审核申请、评阅周报、给学生打分 |
| 企业 | 发布岗位、审核申请、给学生打分 |
| 管理员 | 用户管理、角色管理、企业管理、实习计划管理、成绩管理 |

### 核心功能

- **用户管理**：用户注册、登录、信息维护、密码修改
- **企业管理**：企业入驻、企业审核
- **岗位管理**：岗位发布、岗位列表、过期自动关闭
- **实习申请**：学生申请、审核流程、状态跟踪
- **周报管理**：周报提交、教师评阅
- **实习报告**：报告提交、审核
- **成绩管理**：教师评分、企业评分、总成绩计算
- **定时任务**：自动结束过期计划、关闭过期岗位

## 技术栈

### 后端
- Java 17
- Spring Boot 3.2.5
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Redis
- JWT 认证

### 前端
- Vue 3
- Naive UI
- Vue Router
- Pinia
- Axios

## 项目结构

```
q912/
├── backend/                          # 后端项目
│   ├── src/main/java/com/internship/
│   │   ├── config/                   # 配置类
│   │   ├── controller/               # 控制器
│   │   ├── service/                  # 服务层
│   │   ├── mapper/                   # 数据访问层
│   │   ├── entity/                   # 实体类
│   │   ├── dto/                      # 数据传输对象
│   │   ├── security/                 # 安全认证
│   │   ├── task/                     # 定时任务
│   │   └── common/                   # 公共类
│   └── src/main/resources/
│       ├── application.yml           # 配置文件
│       └── db/init.sql               # 数据库脚本
│
└── frontend/                         # 前端项目
    ├── src/
    │   ├── views/                    # 页面组件
    │   ├── components/               # 公共组件
    │   ├── api/                      # API 接口
    │   ├── stores/                   # 状态管理
    │   ├── router/                   # 路由配置
    │   └── utils/                    # 工具函数
    └── package.json
```

## 快速开始

### 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis

### 数据库配置

1. 创建数据库
```sql
CREATE DATABASE test_q912_1 DEFAULT CHARACTER SET utf8mb4;
```

2. 执行初始化脚本
```bash
mysql -u root -p test_q912_1 < backend/src/main/resources/db/init.sql
```

3. 修改数据库配置（如需要）
```yaml
# backend/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test_q912_1
    username: your_username
    password: your_password
```

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端服务地址：http://localhost:8080

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端访问地址：http://localhost:3000

## 默认账号

系统初始化后需要自行注册账号，或通过管理员创建用户。

## API 接口

### 认证模块
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册
- `GET /auth/current` - 获取当前用户信息
- `POST /auth/changePassword` - 修改密码

### 用户模块
- `GET /user/page` - 用户列表（分页）
- `POST /user` - 创建用户
- `PUT /user` - 更新用户信息
- `DELETE /user/{id}` - 删除用户
- `POST /user/role` - 分配角色

### 岗位模块
- `GET /position/page` - 岗位列表
- `GET /position/available` - 可申请岗位列表
- `GET /position/{id}` - 岗位详情
- `POST /position` - 发布岗位

### 申请模块
- `GET /application/page` - 申请列表
- `GET /application/my` - 我的申请
- `GET /application/enterprise` - 企业申请列表
- `GET /application/teacher` - 教师申请列表
- `POST /application/apply` - 申请实习
- `POST /application/approve/{id}` - 通过申请
- `POST /application/reject/{id}` - 拒绝申请

### 成绩模块
- `GET /application/score` - 获取成绩
- `POST /application/score/teacher` - 教师评分
- `POST /application/score/enterprise` - 企业评分

## 定时任务

系统每天凌晨自动执行以下任务：
- 自动结束已过期的实习计划
- 自动关闭已过期的实习岗位

## 开发说明

### 用户类型
- 1: 学生
- 2: 教师
- 3: 企业
- 4: 管理员

### 申请状态
- 0: 待审核
- 1: 已通过
- 2: 已拒绝

### 计划状态
- 0: 草稿
- 1: 进行中
- 2: 已结束

## 许可证

非本团队成员不得对外销售，只允许免费直接使用，销售者将追究责任
