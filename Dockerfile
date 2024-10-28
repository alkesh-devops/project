FROM maven:3.6.3-openjdk-8-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests=true
FROM openjdk:8-jdk-alpine
WORKDIR /app
VOLUME /tmp
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8989
CMD ["java", "-jar", "app.jar"]