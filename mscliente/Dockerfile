FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar

ARG EUREKA_SERVER=localhost

ENTRYPOINT ["java", "-jar", "app.jar"]



# COMANDOS DOCKER

# docker container rm msclientes


# 1- docker build -t msclientes .
# 2- docker run --name msclientes --network msnetwork -e EUREKA_SERVER=eurekaserver -d msclientes