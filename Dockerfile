# Stage 1: Compile and Build the application using system maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# 🎯 CHANGED: Use 'mvn' directly instead of './mvnw' to bypass permission crashes
RUN mvn clean package -DskipTests

# Stage 2: Run the application package
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]