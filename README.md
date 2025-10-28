<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">antrcp-template</h1>
<h4 align="center">Maven Tych构建Eclipse RCP应用的模板，让Eclipse应用程序构建变得简单、优雅！</h4>
<p align="center">
	<a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html"><img src="https://img.shields.io/badge/JDK-21+-green.svg"></a>
	<a href="https://maven.apache.org"><img src="https://img.shields.io/badge/maven-v3.9.9-blue"></a>
	<a href="https://download.eclipse.org/releases/2024-12/202412041000/"><img src="https://img.shields.io/badge/TargetPlatform-202412-blue"></a>
	<a href="https://www.eclipse.org/downloads"><img src="https://img.shields.io/badge/Eclipse%20IDE-提供支持-blue.svg"></a>
	<a href=""><img src="https://img.shields.io/badge/系统-win%20%7C%20mac%20%7C%20linux-007EC6"></a>
	<a href="https://gitee.com/antzou/antrcp-template/blob/master/LICENSE"><img src="https://img.shields.io/:license-epl2.0-green.svg"></a>
</p>


---

### antrcp-template 介绍

antrcp-template 是Eclipse RCP (Rich Client Platform) 项目的 Maven 多模块结构，使用 Tycho 作为构建工具。


## 📦 模块详细说明

### 1. **bundles 模块**
**作用**: 包含所有的 OSGi bundle（插件）

**内容**:
- 核心业务逻辑代码
- UI 组件
- 服务实现
- 扩展点实现

**特点**:
- 每个 bundle 都是一个独立的模块
- 可以单独开发、测试和部署
- 遵循 OSGi 规范

### 2. **features 模块**
**作用**: 功能特性聚合和管理

**内容**:
- 将相关的 bundles 组合成完整的特性
- 定义产品包含哪些功能
- 管理依赖关系和版本

**用途**:
- 用户可以选择安装特定的 feature，而不是单个 bundle
- 便于功能模块化管理

### 3. **releng 模块**
**作用**: Release Engineering（发布工程）

**内容**:
- 产品构建配置
- 目标平台定义
- 产品配置文件
- 更新站点配置
- 打包和分发配置

**重要性**: 负责整个产品的最终组装和发布

### 4. **tests 模块**
**作用**: 测试代码

**内容**:
- 单元测试
- 集成测试
- UI 测试（如 SWTBot 测试）

**具体位置**: `tests/antzou.template.tests` 是具体的测试项目


---

## 🚀 快速开始

### 环境要求
- JDK 21+
- Maven 3.9.9+
- Eclipse IDE（eclipse-jee-2024-12-R推荐）

### 构建命令
```bash
# 清理项目
mvn clean

# 运行测试
mvn verify 

# 打包产品
mvn clean verify -Dskip.ui-tests=false
```

### e3实践
- [[ antrcp-assistant ]](https://gitee.com/antzou/antrcp-assistant)：办公效率工具集
- [[ antrcp-crm ]](https://gitee.com/antzou/antrcp-crm)：客户关系管家

