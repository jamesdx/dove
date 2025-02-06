#!/bin/bash

# Exit on error
set -e

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Default values
PROFILE="dev"
REGISTRY="docker.io"
NAMESPACE="dove"
VERSION="latest"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
        -p|--profile)
            PROFILE="$2"
            shift
            shift
            ;;
        -r|--registry)
            REGISTRY="$2"
            shift
            shift
            ;;
        -n|--namespace)
            NAMESPACE="$2"
            shift
            shift
            ;;
        -v|--version)
            VERSION="$2"
            shift
            shift
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

echo "Deploying project with profile: $PROFILE"

# Function to deploy a service
deploy_service() {
    local service=$1
    echo "Deploying $service..."
    
    # Push Docker image
    if [[ "$PROFILE" == "prod" ]]; then
        docker tag "$NAMESPACE/$service:$VERSION" "$REGISTRY/$NAMESPACE/$service:$VERSION"
        docker push "$REGISTRY/$NAMESPACE/$service:$VERSION"
    fi
    
    # Apply Kubernetes manifests
    if [ -d "$PROJECT_ROOT/$service/k8s" ]; then
        kubectl apply -f "$PROJECT_ROOT/$service/k8s" --namespace="$NAMESPACE-$PROFILE"
    fi
}

# Create namespace if it doesn't exist
kubectl create namespace "$NAMESPACE-$PROFILE" --dry-run=client -o yaml | kubectl apply -f -

# Deploy core services in order
SERVICES=(
    "dove-gateway"
    "dove-auth"
    "dove-user"
    "dove-security"
    "dove-monitor"
)

for service in "${SERVICES[@]}"; do
    deploy_service "$service"
done

echo "Deployment completed successfully!"

# Wait for services to be ready
echo "Waiting for services to be ready..."
kubectl wait --for=condition=ready pod -l app.kubernetes.io/part-of=dove --timeout=300s --namespace="$NAMESPACE-$PROFILE"

# Show deployment status
echo "Deployment status:"
kubectl get pods --namespace="$NAMESPACE-$PROFILE" 