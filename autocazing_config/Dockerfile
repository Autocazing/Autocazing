# Use Zulu OpenJDK for the build stage
FROM azul/zulu-openjdk:17 as builder

# Set the working directory in the Docker image
WORKDIR /app

# Copy the Gradle configuration files first (for caching)
COPY build.gradle settings.gradle /app/

# Copy the Gradle wrapper and scripts
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Grant execution rights on the gradlew
RUN chmod +x /app/gradlew

# Copy the source code
COPY src /app/src

# Package the application
RUN ./gradlew clean build

# Use Zulu OpenJDK for the runtime stage
FROM azul/zulu-openjdk:17

# Copy the jar from the build stage to the production image
COPY --from=builder /app/build/libs/*.jar /app/config-server.jar

# Specify the entrypoint to start the application
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]