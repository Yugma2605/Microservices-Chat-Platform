# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container (update with correct JAR name)
COPY target/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar

# Expose the port for the API gateway
EXPOSE 9000

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]
