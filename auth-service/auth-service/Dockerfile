# Start with a base image containing Java
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar

# Expose the port for the auth service
EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]
