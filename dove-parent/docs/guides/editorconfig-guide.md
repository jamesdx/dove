# EditorConfig 使用指南
# EditorConfig Usage Guide

## 简介 | Introduction

EditorConfig 帮助开发人员在不同的编辑器和 IDE 之间保持一致的编码风格。它由一个定义编码风格的文件格式和一组编辑器插件组成，这些插件使编辑器能够读取文件格式并遵循定义的样式。

EditorConfig helps maintain consistent coding styles for multiple developers working on the same project across various editors and IDEs. It consists of a file format for defining coding styles and a collection of editor plugins that enable editors to read the file format and adhere to defined styles.

## 支持的编辑器 | Supported Editors

### 内置支持（无需插件）| Built-in Support (No Plugin Required)
- IntelliJ IDEA
- WebStorm
- Android Studio
- Visual Studio Code
- GitHub
- Eclipse IDE 2021-12+

### 需要插件的编辑器 | Editors Requiring Plugins
- Sublime Text
- Vim
- Emacs
- Atom
- Eclipse (older versions)
- Notepad++

## 安装说明 | Installation Guide

### IntelliJ IDEA/WebStorm
1. 无需安装插件，已内置支持
2. 确保在设置中启用 EditorConfig 支持：
   - 转到 `Settings/Preferences` -> `Editor` -> `Code Style`
   - 勾选 `Enable EditorConfig Support`

### VS Code
1. 安装 EditorConfig 插件：
   - 打开 VS Code
   - 转到扩展市场 (Ctrl+Shift+X)
   - 搜索 "EditorConfig"
   - 安装 "EditorConfig for VS Code"

### Eclipse
1. 对于 Eclipse IDE 2021-12 及更新版本：内置支持
2. 对于旧版本：
   - 转到 `Help` -> `Eclipse Marketplace`
   - 搜索 "EditorConfig"
   - 安装 "EditorConfig Eclipse"

### Sublime Text
1. 首先安装 Package Control
2. 通过 Package Control 安装 EditorConfig：
   - 按 `Ctrl+Shift+P` (Windows/Linux) 或 `Cmd+Shift+P` (macOS)
   - 输入 "Package Control: Install Package"
   - 搜索并安装 "EditorConfig"

## 配置说明 | Configuration Guide

我们的 `.editorconfig` 文件包含以下主要配置：

### 通用配置 | General Settings
```editorconfig
[*]
end_of_line = lf                 # 使用 Unix 风格的换行符
insert_final_newline = true      # 文件末尾插入空行
charset = utf-8                  # 使用 UTF-8 编码
trim_trailing_whitespace = true  # 删除行尾空格
```

### Java 文件配置 | Java Files Configuration
```editorconfig
[*.{java,groovy,gradle}]
indent_style = space            # 使用空格缩进
indent_size = 4                 # 缩进大小为 4 个空格
continuation_indent_size = 8    # 续行缩进为 8 个空格
max_line_length = 120          # 最大行长度为 120 字符
```

### XML 文件配置 | XML Files Configuration
```editorconfig
[*.{xml,xsd}]
indent_style = space
indent_size = 4
continuation_indent_size = 8
max_line_length = 120
```

### YAML 文件配置 | YAML Files Configuration
```editorconfig
[*.{yml,yaml}]
indent_style = space
indent_size = 2                # YAML 文件使用 2 空格缩进
```

## 使用建议 | Usage Recommendations

1. **提交前检查**
   - 提交代码前使用 `git diff` 检查格式变更
   - 确保没有不必要的格式改动

2. **团队协作**
   - 所有团队成员都应该安装相应的 EditorConfig 插件
   - 在克隆项目后立即检查编辑器的 EditorConfig 设置

3. **格式化快捷键**
   - IntelliJ IDEA: `Ctrl + Alt + L` (Windows/Linux) 或 `Cmd + Alt + L` (macOS)
   - VS Code: `Shift + Alt + F`
   - Eclipse: `Ctrl + Shift + F`

4. **常见问题解决**
   - 如果格式化不生效，检查：
     - 编辑器是否正确安装和配置 EditorConfig 插件
     - `.editorconfig` 文件是否在项目根目录
     - 文件是否符合配置中的文件类型规则

## 特定文件类型说明 | Specific File Type Notes

### Markdown 文件 | Markdown Files
```editorconfig
[*.md]
trim_trailing_whitespace = false  # Markdown 中允许行尾空格（用于换行）
max_line_length = off            # 不限制行长度
```

### Shell 脚本 | Shell Scripts
```editorconfig
[*.sh]
end_of_line = lf                # 必须使用 Unix 换行符
indent_style = space
indent_size = 2
```

### 前端文件 | Frontend Files
```editorconfig
[*.{js,ts,jsx,tsx,css,scss,html}]
indent_style = space
indent_size = 2                 # 使用 2 空格缩进
max_line_length = 100          # 最大行长度 100
```

## 验证配置 | Verify Configuration

要验证 EditorConfig 是否正常工作：

1. 打开任意源代码文件
2. 故意添加一些不符合规范的格式（如额外的空格）
3. 保存文件或使用格式化快捷键
4. 确认格式是否自动按照规范调整

## 注意事项 | Important Notes

1. `.editorconfig` 文件应该放在项目根目录
2. 配置采用就近原则，离文件最近的配置生效
3. 设置 `root = true` 防止向上级目录查找配置
4. 某些编辑器可能需要重启才能识别新的配置

## 更多资源 | Additional Resources

- [EditorConfig 官方文档](https://editorconfig.org)
- [EditorConfig Properties](https://github.com/editorconfig/editorconfig/wiki/EditorConfig-Properties)
- [常见编辑器插件列表](https://editorconfig.org/#download) 