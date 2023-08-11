# Stage 1: Build the application using Maven
FROM jelastic/maven:3.9.4-openjdk-20.0.2 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean install -DskipTests

# Stage 2: Copy the built artifact and run it
FROM openjdk:20-jdk-oracle
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
