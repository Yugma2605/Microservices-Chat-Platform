# Start with a base image containing Java
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/discoveryservice-0.0.1-SNAPSHOT.jar /app/discovery-service.jar

# Expose the port for the discovery service
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "/app/discovery-service.jar"]
