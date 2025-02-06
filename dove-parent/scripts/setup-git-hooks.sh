#!/bin/bash

# 设置颜色
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# 检查命令是否存在
check_command() {
    local command=$1
    if ! command -v "$command" &> /dev/null; then
        print_message "$RED" "错误: $command 未安装"
        return 1
    fi
    return 0
}

# 检查 Python 和 pip
check_python() {
    print_message "$YELLOW" "检查 Python 环境..."
    if ! check_command "python3"; then
        print_message "$RED" "请安装 Python 3"
        exit 1
    fi
    if ! check_command "pip3"; then
        print_message "$RED" "请安装 pip3"
        exit 1
    fi
    print_message "$GREEN" "Python 环境检查通过"
}

# 检查 Git
check_git() {
    print_message "$YELLOW" "检查 Git 环境..."
    if ! check_command "git"; then
        print_message "$RED" "请安装 Git"
        exit 1
    fi
    print_message "$GREEN" "Git 环境检查通过"
}

# 安装 pre-commit
install_pre_commit() {
    print_message "$YELLOW" "安装 pre-commit..."
    if ! check_command "pre-commit"; then
        pip3 install pre-commit
        if [ $? -ne 0 ]; then
            print_message "$RED" "pre-commit 安装失败"
            exit 1
        fi
    else
        print_message "$GREEN" "pre-commit 已安装"
    fi
    print_message "$GREEN" "pre-commit 安装完成"
}

# 安装 Git hooks
install_git_hooks() {
    print_message "$YELLOW" "安装 Git hooks..."

    # 检查 .pre-commit-config.yaml 是否存在
    if [ ! -f ".pre-commit-config.yaml" ]; then
        print_message "$RED" "错误: .pre-commit-config.yaml 文件不存在"
        exit 1
    fi

    # 安装 hooks
    pre-commit install
    if [ $? -ne 0 ]; then
        print_message "$RED" "Git hooks 安装失败"
        exit 1
    fi

    # 更新 hooks
    pre-commit autoupdate
    if [ $? -ne 0 ]; then
        print_message "$RED" "Git hooks 更新失败"
        exit 1
    fi

    print_message "$GREEN" "Git hooks 安装完成"
}

# 检查 Java 环境
check_java() {
    print_message "$YELLOW" "检查 Java 环境..."
    if ! check_command "java"; then
        print_message "$RED" "请安装 Java"
        exit 1
    fi
    if ! check_command "javac"; then
        print_message "$RED" "请安装 Java Development Kit (JDK)"
        exit 1
    fi
    print_message "$GREEN" "Java 环境检查通过"
}

# 检查 Maven
check_maven() {
    print_message "$YELLOW" "检查 Maven 环境..."
    if ! check_command "mvn"; then
        print_message "$RED" "请安装 Maven"
        exit 1
    fi
    print_message "$GREEN" "Maven 环境检查通过"
}

# 主函数
main() {
    print_message "$YELLOW" "开始安装 Git hooks..."

    # 环境检查
    check_python
    check_git
    check_java
    check_maven

    # 安装 pre-commit 和 hooks
    install_pre_commit
    install_git_hooks

    print_message "$GREEN" "安装完成！"
    print_message "$YELLOW" "提示：首次提交可能需要较长时间，因为需要下载和安装依赖"
}

# 执行主函数
main
