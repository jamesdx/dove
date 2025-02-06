# Git Hooks 使用指南
# Git Hooks Usage Guide

## 简介 | Introduction

本文档介绍了项目中使用的 Git pre-commit hooks 配置，这些钩子可以在代码提交前自动进行代码检查和格式化。

This document introduces the Git pre-commit hooks configuration used in the project, which automatically perform code checks and formatting before commits.

## 安装步骤 | Installation Steps

### 1. 安装 pre-commit | Install pre-commit
```bash
# 使用 pip 安装
pip install pre-commit

# 或使用 Homebrew (macOS)
brew install pre-commit

# 或使用 conda
conda install -c conda-forge pre-commit
```

### 2. 安装 Git hooks | Install Git hooks
在项目根目录执行：
Execute in the project root directory:
```bash
pre-commit install
```

### 3. 更新 hooks | Update hooks
定期更新 hooks 以获取最新版本：
Regularly update hooks to get the latest versions:
```bash
pre-commit autoupdate
```

## 配置说明 | Configuration Guide

项目的 pre-commit hooks 配置位于 `.pre-commit-config.yaml` 文件中，包含以下主要检查：

The pre-commit hooks configuration is located in the `.pre-commit-config.yaml` file and includes the following main checks:

### 1. 基础文件检查 | Basic File Checks
- 删除行尾空格 | Remove trailing whitespace
- 确保文件以换行符结束 | Ensure files end with newline
- 检查 YAML/XML/JSON 语法 | Check YAML/XML/JSON syntax
- 检查合并冲突 | Check merge conflicts
- 检查文件名大小写冲突 | Check filename case conflicts
- 检测私钥文件 | Detect private keys

### 2. 代码格式化 | Code Formatting
- Java 代码格式化（使用 Google Java Format）| Java code formatting (using Google Java Format)
- YAML 文件格式化 | YAML file formatting
- HTML 文件验证 | HTML file validation

### 3. 代码质量检查 | Code Quality Checks
- Checkstyle 检查 | Checkstyle checks
- PMD 检查 | PMD checks
- SpotBugs 检查 | SpotBugs checks

### 4. 自定义检查 | Custom Checks
- 禁止提交二进制文件 | Forbid binary files
- 检查文件大小（>1MB） | Check file size (>1MB)
- 检查 Java main 方法 | Check Java main methods
- 检查 System.out.println | Check System.out.println

## 使用方法 | Usage

### 1. 常规提交 | Normal Commit
```bash
git add .
git commit -m "your message"
```
hooks 会自动运行并检查代码。

Hooks will automatically run and check the code.

### 2. 跳过检查 | Skip Checks
```bash
git commit -m "your message" --no-verify
```
不推荐使用，仅用于特殊情况。

Not recommended, only for special cases.

### 3. 手动运行检查 | Manual Check
```bash
pre-commit run --all-files  # 检查所有文件 | Check all files
pre-commit run             # 只检查暂存的文件 | Check only staged files
```

## 常见问题 | Common Issues

### 1. 格式化失败 | Formatting Fails
- 检查 Java 和 YAML 文件的语法
- 使用 IDE 的格式化功能预处理
- 查看具体错误信息进行修复

### 2. 代码质量检查失败 | Code Quality Check Fails
- 查看详细的错误报告
- 修复 Checkstyle/PMD/SpotBugs 报告的问题
- 必要时在代码中添加合适的抑制注解

### 3. 文件大小超限 | File Size Exceeds Limit
- 检查是否误提交了大文件
- 考虑使用 Git LFS 管理大文件
- 必要时调整大小限制

## 最佳实践 | Best Practices

1. **提交前的准备 | Pre-commit Preparation**
   - 使用 IDE 的格式化功能
   - 运行单元测试
   - 检查代码中的 TODO 和 FIXME

2. **增量提交 | Incremental Commits**
   - 小批量、频繁提交
   - 相关改动放在同一提交
   - 提供清晰的提交信息

3. **团队协作 | Team Collaboration**
   - 确保所有团队成员安装 pre-commit
   - 统一使用相同的代码风格配置
   - 定期更新 hooks 配置

## 配置维护 | Configuration Maintenance

1. **更新配置 | Update Configuration**
   - 定期检查并更新 hooks 版本
   - 根据项目需求调整检查规则
   - 在团队中同步配置变更

2. **性能优化 | Performance Optimization**
   - 避免过于严格的检查规则
   - 合理设置文件排除规则
   - 必要时调整检查的并行度

3. **问题反馈 | Issue Feedback**
   - 收集团队成员的使用反馈
   - 及时解决配置问题
   - 持续优化检查规则

## 更多资源 | Additional Resources

- [pre-commit 官方文档](https://pre-commit.com/)
- [Google Java Format](https://github.com/google/google-java-format)
- [Checkstyle 文档](https://checkstyle.sourceforge.io/)
- [PMD 文档](https://pmd.github.io/)
- [SpotBugs 文档](https://spotbugs.github.io/) 
