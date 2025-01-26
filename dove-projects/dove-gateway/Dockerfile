FROM openjdk:17-jdk-slim

MAINTAINER helix team <support@helix.com>

# Set environment variables
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Create app directory
RUN mkdir -p /app

# Add application
COPY target/dove-gateway-1.0.0-SNAPSHOT.jar /app/app.jar

# Set working directory
WORKDIR /app

# Expose port
EXPOSE 9000

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 