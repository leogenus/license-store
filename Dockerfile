# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-focal AS build

WORKDIR /app

# Copy the Gradle Wrapper and the build file
COPY gradlew build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
RUN chmod +x gradlew
RUN ./gradlew dependencies

# Copy the source code and build the application
COPY src ./src
RUN ./gradlew bootJar --info --no-daemon

# Stage 2: Prepare the runtime image
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

CMD ["mkdir", "/app/config"]

# Copy only the built artifact from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/src/main/resources/*.yml ./config/

#VOLUME /app/config
EXPOSE 80
ENTRYPOINT ["java", "-Dspring.config.location=/app/config/", "-jar", "app.jar"]

