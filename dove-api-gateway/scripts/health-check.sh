#!/bin/bash

# 设置错误时退出
set -e

echo "Performing health check for Dove API Gateway..."

# 获取服务IP
EXTERNAL_IP=$(kubectl get svc dove-api-gateway -n dove -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
if [ -z "$EXTERNAL_IP" ]; then
    echo "Error: External IP not found"
    exit 1
fi

# 检查服务健康状态
echo "Checking service health status..."
HEALTH_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://$EXTERNAL_IP:8080/actuator/health)

if [ "$HEALTH_STATUS" == "200" ]; then
    echo "Service is healthy (HTTP 200)"
    
    # 获取详细的健康信息
    echo "Fetching detailed health information..."
    curl -s http://$EXTERNAL_IP:8080/actuator/health | jq .
    
    # 检查 Pod 状态
    echo -e "\nChecking Pod status..."
    kubectl get pods -n dove -l app=dove-api-gateway -o wide
    
    # 检查资源使用情况
    echo -e "\nChecking resource usage..."
    kubectl top pod -n dove -l app=dove-api-gateway
    
    exit 0
else
    echo "Service is not healthy (HTTP $HEALTH_STATUS)"
    
    # 输出最近的日志
    echo -e "\nRecent logs from pods:"
    kubectl logs -n dove -l app=dove-api-gateway --tail=50
    
    # 描述 Pod 状态
    echo -e "\nPod description:"
    kubectl describe pods -n dove -l app=dove-api-gateway
    
    exit 1
fi 