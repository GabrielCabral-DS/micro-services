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

# docker container rm msavaliadorcredito


# 1- docker build -t msavaliadorcredito .
# 2- docker run --name msavaliadorcredito --network msnetwork -e RABBITMQ_SERVER=cursomsrabbitmq -e EUREKA_SERVER=eurekaserver -d msavaliadorcredito