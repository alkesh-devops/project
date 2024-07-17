# FROM openjdk:11
# FROM maven:3.8-jdk-11 as maven_build
# WORKDIR /app
# EXPOSE 8989
# ARG JAR_FILE=target/*.jar 
# COPY pom.xml pom.xml
# COPY src src
# RUN mvn clean package
# COPY ./target/*.jar /app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]  

# FROM openjdk:11
# VOLUME /tmp
# EXPOSE 8080
# ARG JAR_FILE=target/*.jar
# ADD ./target/livelo-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

# FROM openjdk:11
# FROM maven:3.8-jdk-11 as maven_build
# COPY pom.xml pom.xml
# COPY src src
# RUN mvn clean package -X
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

# FROM maven:3.8.4-openjdk-17-slim 
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests
# EXPOSE 8080
# CMD ["java", "-jar", "target/your-application.jar"]

 -DskipTests=true


 <!-- apiVersion: apps/v1
kind: Deployment
metadata:
  name: your-app-deployment
spec:
  replicas: 1 
  selector:
    matchLabels:
      app: your-app
  template:
    metadata:
      labels:
        app: your-app
    spec:
      containers:
        - name: your-app-container
          image: spring-boot-1:latest
           -->

# apiVersion: apps/v1
# kind: Deployment
# metadata:
#   name: java-deployment
#   labels:
#     app: java
# spec:
#   replicas: 1
#   selector:
#     matchLabels:
#       app: java
#   template:
#     metadata:
#       labels:
#         app: java
#     spe
#       containers:
#         - name: java-spring-boot
#           image: spring-boot-1:latest
#           ports:
#             - containerPort: 80


compose url - http://localhost:2910/cidades

docker image - http://localhost:2000/cidades



apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-deployment
spec:
  replicas: 1
  selector:
     matchLabels:
       app: java
   template:
     metadata:
  #     labels:
  #       app: java
  #   spec:
  #     containers:
  #     - name: java-spring-boot
  #       image: spring-boot-1:latest
  #       ports:
  #       - containerPort: 8080