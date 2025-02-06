#!/bin/bash

# Exit on error
set -e

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Default profile
PROFILE="dev"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
        -p|--profile)
            PROFILE="$2"
            shift
            shift
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

echo "Building project with profile: $PROFILE"

# Clean and install parent project
cd "$PROJECT_ROOT"
./mvnw clean install -P"$PROFILE" -DskipTests

# Run tests if not explicitly skipped
if [[ "$SKIP_TESTS" != "true" ]]; then
    echo "Running tests..."
    ./mvnw verify -P"$PROFILE"
fi

# Build Docker images if in prod profile
if [[ "$PROFILE" == "prod" ]]; then
    echo "Building Docker images..."
    ./mvnw dockerfile:build -P"$PROFILE"
fi

echo "Build completed successfully!" 