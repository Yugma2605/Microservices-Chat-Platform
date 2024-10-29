# Start with a base image containing Java
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/upload-service-0.0.1-SNAPSHOT.jar /app/upload-service.jar

# Expose the port for the upload service
EXPOSE 8084

# Run the application
ENTRYPOINT ["java", "-jar", "/app/upload-service.jar"]
