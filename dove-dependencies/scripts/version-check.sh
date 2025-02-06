#!/bin/bash

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting dependency version check...${NC}"

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven is not installed. Please install Maven first.${NC}"
    exit 1
fi

# 检查依赖更新
echo -e "${YELLOW}Checking for dependency updates...${NC}"
mvn versions:display-dependency-updates -DprocessDependencyManagement=false

# 检查插件更新
echo -e "${YELLOW}Checking for plugin updates...${NC}"
mvn versions:display-plugin-updates

# 检查属性更新
echo -e "${YELLOW}Checking for property updates...${NC}"
mvn versions:display-property-updates

# 生成更新报告
echo -e "${YELLOW}Generating update report...${NC}"
mvn versions:dependency-updates-report
mvn versions:plugin-updates-report
mvn versions:property-updates-report

echo -e "${GREEN}Version check completed. Please check target/site for detailed reports.${NC}"

# 检查是否有安全漏洞
echo -e "${YELLOW}Checking for security vulnerabilities...${NC}"
mvn org.owasp:dependency-check-maven:check

echo -e "${GREEN}All checks completed.${NC}" 