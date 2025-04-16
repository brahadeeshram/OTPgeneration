# Use a Maven image to build the project
FROM maven:3.8.6-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# Build the project using Maven
RUN mvn clean package -DskipTests

# Second stage: use a smaller image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy jar from the first build stage
COPY --from=build /app/target/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
