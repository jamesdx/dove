#!/bin/bash

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting dependency analysis...${NC}"

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven is not installed. Please install Maven first.${NC}"
    exit 1
fi

# 分析依赖树
echo -e "${YELLOW}Analyzing dependency tree...${NC}"
mvn dependency:tree -DoutputFile=target/dependency-tree.txt

# 分析依赖使用情况
echo -e "${YELLOW}Analyzing dependency usage...${NC}"
mvn dependency:analyze

# 检查重复依赖
echo -e "${YELLOW}Checking for duplicate dependencies...${NC}"
mvn dependency:analyze-duplicate

# 检查依赖冲突
echo -e "${YELLOW}Checking for dependency conflicts...${NC}"
mvn enforcer:enforce -Drules=dependencyConvergence

# 生成依赖报告
echo -e "${YELLOW}Generating dependency reports...${NC}"
mvn project-info-reports:dependencies

echo -e "${GREEN}Dependency analysis completed. Please check target directory for reports.${NC}"

# 检查许可证
echo -e "${YELLOW}Checking licenses...${NC}"
mvn license:aggregate-add-third-party

echo -e "${GREEN}All analysis completed.${NC}" 