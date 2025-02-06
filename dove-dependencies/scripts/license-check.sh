#!/bin/bash

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting license check...${NC}"

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven is not installed. Please install Maven first.${NC}"
    exit 1
fi

# 生成许可证报告
echo -e "${YELLOW}Generating license report...${NC}"
mvn license:aggregate-download-licenses
mvn license:aggregate-add-third-party

# 检查是否有GPL许可证
echo -e "${YELLOW}Checking for GPL licenses...${NC}"
if grep -i "GPL" target/generated-sources/license/THIRD-PARTY.txt; then
    echo -e "${RED}WARNING: GPL licensed dependencies found!${NC}"
    echo -e "${RED}Please review these dependencies as they may conflict with commercial use.${NC}"
fi

# 检查未知许可证
echo -e "${YELLOW}Checking for unknown licenses...${NC}"
if grep -i "unknown" target/generated-sources/license/THIRD-PARTY.txt; then
    echo -e "${RED}WARNING: Dependencies with unknown licenses found!${NC}"
    echo -e "${RED}Please review these dependencies and verify their licenses.${NC}"
fi

# 生成详细的许可证报告
echo -e "${YELLOW}Generating detailed license report...${NC}"
mvn project-info-reports:dependencies

echo -e "${GREEN}License check completed. Please check target directory for reports.${NC}"

# 提示查看报告
echo -e "${YELLOW}Reports generated:${NC}"
echo -e "1. ${GREEN}target/generated-sources/license/THIRD-PARTY.txt${NC} - Third party licenses"
echo -e "2. ${GREEN}target/site/dependencies.html${NC} - Detailed dependency report" 