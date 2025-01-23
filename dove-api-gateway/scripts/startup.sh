#!/bin/bash

# 设置错误时退出
set -e

echo "Starting Dove API Gateway deployment..."

# 创建 namespace (如果不存在)
kubectl create namespace dove --dry-run=client -o yaml | kubectl apply -f -

# 应用 Kubernetes 配置
echo "Applying Kubernetes configurations..."
kubectl apply -f k8s/configmap.yml
kubectl apply -f k8s/secret.yml
kubectl apply -f k8s/deployment.yml
kubectl apply -f k8s/service.yml

# 等待 deployment 就绪
echo "Waiting for deployment to be ready..."
kubectl rollout status deployment/dove-api-gateway -n dove

# 检查服务状态
echo "Checking service status..."
kubectl get svc dove-api-gateway -n dove

echo "Deployment completed successfully!"

# 显示访问信息
EXTERNAL_IP=$(kubectl get svc dove-api-gateway -n dove -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
if [ -n "$EXTERNAL_IP" ]; then
    echo "Service is accessible at: http://$EXTERNAL_IP:8080"
else
    echo "Waiting for external IP to be assigned..."
    echo "Use 'kubectl get svc dove-api-gateway -n dove' to check the status"
fi 