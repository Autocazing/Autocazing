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
RUN ./gradlew clean build -x test

# Use Zulu OpenJDK for the runtime stage
FROM azul/zulu-openjdk:17

# ARG to receive profile name during build( ex) docker build --build-arg PROFILE=dev -t your_image_name . )
# if no --build-arg option given, local will applied to image)
ARG PROFILE=local

# Set environment variable for Spring profile from ARG
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

# Copy the jar from the build stage to the production image
COPY --from=builder /app/build/libs/*.jar /app/inventory-service.jar

# Specify the entrypoint to start the application
ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]