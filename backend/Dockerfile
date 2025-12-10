
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# Copy Maven descriptor first
COPY pom.xml .

RUN mvn -q dependency:go-offline

# Now copy the source
COPY src ./src

# Build the JAR
RUN mvn -q clean package -DskipTests


FROM eclipse-temurin:25-jre


WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
