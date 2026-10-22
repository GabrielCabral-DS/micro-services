FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar

ARG EUREKA_SERVER=localhost
ARG KEYCLOAK_SERVER=localhost
ARG KEYCLOAK_PORT=8081

ENTRYPOINT ["java", "-jar", "app.jar"]



# COMANDOS DOCKER

# docker container rm mscloudgateway


# 1- docker build -t mscloudgateway .
# 2- docker run --name mscloudgateway -p 8080:8080 -e EUREKA_SERVER=eurekaserver -e KEYCLOAK_SERVER=keycloak -e KEYCLOAK_PORT=8080 --network msnetwork -d mscloudgateway