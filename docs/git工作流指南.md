# Git工作流指南
## 一、Git 的核心命令总结
| 命令             | 作用        |
| -------------- | --------- |
| `git status`   | 查看当前状态    |
| `git add`      | 添加文件到暂存区  |
| `git commit`   | 提交修改      |
| `git log`      | 查看历史      |
| `git diff`     | 查看修改内容    |
| `git checkout` | 切换分支/恢复文件 |
| `git branch`   | 管理分支      |

---

## 二、完整开发流程
### 1. 每日开始
```bash
# 进入项目目录
cd /你的项目路径/CourseSelect

# 拉取最新代码（防止冲突）
git pull origin master
```

### 2. 功能开发
```bash
# 创建并切换到功能分支
git checkout -b feature/功能名

# 例子：开发登录功能
git checkout -b feature/login-optimization
```

### 3. 开发过程中
```bash
# 随时查看修改了哪些文件
git status

# 查看具体修改内容
git diff

# 查看某个文件的修改
git diff src/main/java/com/example/controller/UserController.java
```

### 4. 提交代码
```bash
# 添加修改到暂存区
git add .

# 或只添加特定文件
git add src/main/java/com/example/service/UserService.java

# 提交（写好提交信息）
git commit -m "feat: 优化登录验证逻辑

- 添加密码强度校验
- 修复手机号格式验证bug
- 优化错误提示信息"

# 推送到远程（如果是分支）
git push -u origin feature/login-optimization
```

### 5. 合并到主分支（功能完成后）
```bash
# 切换回主分支
git checkout master

# 拉取最新
git pull origin master

# 合并功能分支
git merge feature/login-optimization

# 推送到远程
git push origin master

# 删除本地分支
git branch -d feature/login-optimization
```
---

## 三、 提交信息规范（快速参考）
| 类型         | 用途    | 示例                    |
| :--------- | :---- | :-------------------- |
| `feat`     | 新功能   | `feat: 添加选课退课功能`      |
| `fix`      | 修复bug | `fix: 修复登录500错误`      |
| `docs`     | 文档/注释 | `docs: 更新API接口文档`     |
| `refactor` | 重构    | `refactor: 优化SQL查询性能` |
| `chore`    | 配置/工具 | `chore: 更新依赖版本`       |


- 分支命名规范：
```text
类型/功能描述

feature/   - 新功能
bugfix/    - 修复bug
hotfix/    - 紧急修复
refactor/  - 重构代码
docs/      - 文档更新
test/      - 测试相关
```
