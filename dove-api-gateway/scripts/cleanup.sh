#!/bin/bash

# 设置错误时退出
set -e

echo "Starting cleanup of Dove API Gateway resources..."

# 删除所有相关资源
echo "Deleting Kubernetes resources..."

# 删除 Service
echo "Deleting Service..."
kubectl delete service dove-api-gateway -n dove --ignore-not-found

# 删除 Deployment
echo "Deleting Deployment..."
kubectl delete deployment dove-api-gateway -n dove --ignore-not-found

# 删除 ConfigMap
echo "Deleting ConfigMap..."
kubectl delete configmap dove-api-gateway-config -n dove --ignore-not-found

# 删除 Secret
echo "Deleting Secret..."
kubectl delete secret dove-api-gateway-secret -n dove --ignore-not-found

# 检查是否还有其他资源在 dove namespace
REMAINING_RESOURCES=$(kubectl get all -n dove -l app=dove-api-gateway 2>/dev/null)
if [ -n "$REMAINING_RESOURCES" ]; then
    echo "Warning: Some resources still exist in the namespace:"
    echo "$REMAINING_RESOURCES"
    
    # 强制删除所有剩余资源
    echo "Force deleting all remaining resources..."
    kubectl delete all -n dove -l app=dove-api-gateway --force --grace-period=0
fi

# 可选：删除 namespace（如果不再需要）
read -p "Do you want to delete the entire 'dove' namespace? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Deleting dove namespace..."
    kubectl delete namespace dove --ignore-not-found
fi

echo "Cleanup completed successfully!" 