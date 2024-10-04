# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY ./pom.xml ./  
COPY ./src ./src    

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/hr_management-1.0.0.jar ./hr_management.jar  
ENTRYPOINT ["java", "-jar", "hr_management.jar"]
