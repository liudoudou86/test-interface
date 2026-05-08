B2B接口测试平台 — 用于 QA 团队对 B2B 后台系统进行接口测试与数据初始化。

## 技术栈

- **基础框架**: Java 8
- **构建工具**: Maven
- **持久层**: MyBatis
- **接口文档**: Swagger, Knife4j
- **测试工具**: Serenity
- **工具库**: Hutool, Lombok, Fastjson, SnakeYaml
- **日志**: Log4j2
- **代码覆盖率**: JaCoCo

## 项目结构

```
src/main/java/com/qa
├── App.java                          # 启动入口 (懒加载)
├── common/
│   └── Response.java                 # 全局统一响应封装
├── config/
│   └── Swagger2Config.java           # Swagger/Knife4j 配置
├── controller/                       # API 接口层
├── service/                          # 业务逻辑层
├── mapper/
│   ├── dto/                          # 数据传输对象
│   └── vo/                           # WSDL 数据视图对象
├── model/
│   ├── api/                          # API 调用封装
│   └── sql/                          # SQL 查询封装
├── task/
│   └── HeartBeatTask.java            # 心跳定时任务
└── utils/                            # 工具类
    ├── FreeMarkerUtil.java
    ├── MysqlUtil.java
    └── YamlUtil.java
```

## 功能模块

### 订单中心
- **正向订单流程**: 创建/冲销正向订单的 Billing 及金税发票
- **退货订单流程**: 创建/冲销退货订单的 Billing 及金税发票
- 自动计算订单折扣（预折、整单折扣、折让）、实付金额

### 合同中心
- 更新合同状态（已盖章、核查通过）
- 更新合同模板状态（已通过）

### OA审批流
- 临时信用申请审批
- 铺底货新增/调整审批
- 整单折扣申请审批
- 合同用印审批

### 信用中心
- 信用/铺底货数据初始化（清除）
- 铺底货到期结转

### 财务中心 / 折让中心
- 财务数据初始化（清除）
- 折让数据初始化（清除）

### MQ查询
- 根据 Topic 查询 RocketMQ 消息中的发票信息

### 定时任务
- 合同中心下发
- 取消申请单自动审核
- 生成购销合同补充协议

## 快速开始

### 配置

`src/main/resources/Application.yml` — 服务端口 (默认 3000)

环境配置位于 `src/main/resources/config/`:
- `stg.yml` — 测试环境
- `uat.yml` — UAT 环境
- `prePro.yml` — 预生产环境

### 启动

```bash
mvn clean package -Dmaven.test.skip=true
java -jar target/test-interface-0.0.1-SNAPSHOT.jar
```

### API 文档

启动后访问：
- Knife4j UI: `http://localhost:3000/doc.html`
- Swagger UI: `http://localhost:3000/swagger-ui.html`

## CI/CD

项目使用 GitLab CI 自动部署，在 `master` 或 `develop` 分支提交时触发构建。

```bash
mvn clean package -Dmaven.test.skip=true
```
