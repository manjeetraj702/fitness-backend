# Stage 1: Compile and Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application package
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Render dynamically reads the PORT environment variable, but Spring runs on 8080 inside the container
ENTRYPOINT ["java", "-jar", "app.jar"]