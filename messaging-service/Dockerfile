# Start with a base image containing Java
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/messaging-service-0.0.1-SNAPSHOT.jar /app/messaging-service.jar

# Expose the port for the messaging service
EXPOSE 8083

# Run the application
ENTRYPOINT ["java", "-jar", "/app/messaging-service.jar"]
