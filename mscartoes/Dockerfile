FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar

ARG RABBITMQ_SERVER=rabbitmq-host
ARG EUREKA_SERVER=localhost

ENTRYPOINT ["java", "-jar", "app.jar"]



# COMANDOS DOCKER

# docker container rm mscartoes
# docker run -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin --network msnetwork --name keycloak quay.io/keycloak/keycloak:18.0.0  start-dev

# 1- docker build -t mscartoes .
# 2- docker run --name mscartoes --network msnetwork -e RABBITMQ_SERVER=cursomsrabbitmq -e EUREKA_SERVER=eurekaserver -d mscartoes