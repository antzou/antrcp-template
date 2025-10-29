# 功能列表

## 📊 界面布局结构

| 区域 | 组件名称 | 元素ID | 功能描述 |
|------|----------|--------|----------|
| **主窗口** | 主窗口 | `antzou.template.application.window.main` | 应用程序主窗口，标题"antrcp-Template" |
| **布局容器** | 可分割布局容器 | `antzou.template.application.main.sashcontainer` | 水平分割的布局容器 |

## 🧭 功能导航区域

| 组件名称 | 元素ID | 功能描述 |
|----------|--------|----------|
| 左侧导航栏 | `antzou.template.application.left.navstack` | 左侧导航面板容器 |
| 操作导航部件 | `antzou.template.application.left.navpart` | 主要功能导航面板，不可关闭 |

## 📝 编辑器区域

| 组件名称 | 元素ID | 功能描述 | 初始状态 |
|----------|--------|----------|----------|
| 右侧编辑器栈 | `antzou.template.application.right.editorstack` | 右侧编辑器区域容器 | 可见 |
| Welcome编辑器 | `com.antzou.application.right.editorpart` | 默认欢迎页面 | 可见 |
| 信息录入部件 | `com.antzou.application.parts.input` | 信息录入功能界面 | 隐藏 |
| 信息查询部件 | `com.antzou.application.parts.query` | 信息查询功能界面 | 隐藏 |
| 信息修改部件 | `com.antzou.application.parts.edit` | 信息修改功能界面 | 隐藏 |

## 🗂️ 菜单系统

### Window 菜单
| 菜单项 | 元素ID | 功能描述 | 命令ID |
|--------|--------|----------|---------|
| Appearance → Hide Toolbar | `window.appearance.hidetoolbar` | 隐藏工具栏 | `cmd_hide_toolbar` |
| Appearance → Hide Status Bar | `window.appearance.hidestatusbar` | 隐藏状态栏 | `cmd_hide_statusbar` |
| Appearance → Toggle Full Screen | `window.appearance.fullscreen` | 切换全屏模式 | `cmd_toggle_fullscreen` |
| Preference | `window.appearance.preference` | 打开偏好设置 | `cmd_preference` |
| Quit | `window.appearance.quit` | 退出应用程序 | `cmd_quit` |

### Help 菜单
| 菜单项 | 元素ID | 功能描述 | 命令ID |
|--------|--------|----------|---------|
| About | `org.eclipse.ui.help.aboutAction` | 显示关于对话框 | `cmd_about` |

## 🛠️ 工具栏功能

| 工具项 | 元素ID | 图标 | 功能描述 | 命令ID |
|--------|--------|------|----------|---------|
| 项目主页 | `com.antzou.application.handleditem.trimbar.top.project` | `sys_project_home.png` | 打开项目主页 | `cmd_project` |
| 百度 | `com.antzou.application.handleditem.trimbar.top.baidu` | `sys_baidu.png` | 打开百度网站 | `cmd_baidu` |
| DeepSeek | `com.antzou.application.handleditem.trimbar.top.deepseek` | `sys_deepseek3.png` | 打开DeepSeek网站 | `cmd_deepseek` |

## 📊 状态栏组件

| 组件 | 元素ID | 功能描述 |
|------|--------|----------|
| 导航状态控件 | `com.antzou.application.statusbar.navigation` | 显示导航状态信息 |
| 填充控件 | `com.antzou.application.statusbar.filler` | 状态栏空白填充区域 |
| 时间显示控件 | `com.antzou.application.statusbar.time` | 显示当前时间信息 |

## 🔧 命令处理器

| 处理器名称 | 元素ID | 功能描述 | 对应命令 |
|------------|--------|----------|----------|
| About处理器 | `com.antzou.application.aboutHandler` | 处理关于对话框显示 | `cmd_about` |
| 退出处理器 | `com.antzou.application.handler.quitCommand` | 处理应用程序退出 | `cmd_quit` |
| 偏好设置处理器 | `com.antzou.application.handler.preferenceCommand` | 处理偏好设置 | `cmd_preference` |
| 项目主页处理器 | `com.antzou.application.handler.projectCommand` | 处理项目主页打开 | `cmd_project` |
| 百度处理器 | `com.antzou.application.handler.baiduCommand` | 处理百度网站打开 | `cmd_baidu` |
| DeepSeek处理器 | `com.antzou.application.handler.deepseekCommand` | 处理DeepSeek网站打开 | `cmd_deepseek` |
| 工具栏可见性处理器 | `com.antzou.application.handler.hidetoolbar` | 控制工具栏显示/隐藏 | `cmd_hide_toolbar` |
| 状态栏可见性处理器 | `com.antzou.application.handler.hidestatusbar` | 控制状态栏显示/隐藏 | `cmd_hide_statusbar` |
| 全屏切换处理器 | `com.antzou.application.handler.togglefullscreen` | 处理全屏模式切换 | `cmd_toggle_fullscreen` |

## 🎨 主题支持

| 主题名称 | 主题ID | 样式表文件 |
|----------|--------|------------|
| Green Theme | `com.antzou.application.theme.green` | `css/e4_green_win.css` |
| Pink Theme | `com.antzou.application.theme.pink` | `css/e4_pink_win.css` |