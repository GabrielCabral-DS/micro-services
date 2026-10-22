[README.md](https://github.com/user-attachments/files/33794252/README.md)
# Microservices com Java, Spring Boot e Spring Cloud

Projeto prático de estudo e implementação de uma arquitetura de
**microserviços com Java 21**, utilizando Spring Boot, Spring Cloud e
ferramentas para descoberta de serviços, roteamento de requisições,
comunicação entre aplicações, segurança e conteinerização com Docker.

> **Status:** projeto finalizado para fins de estudo.\
> **Java:** 21 (LTS) · **Maven:** 3.9.6 · **Docker:** imagens baseadas
> em Eclipse Temurin 21.

## 📌 Visão geral

O projeto demonstra como organizar uma aplicação distribuída em serviços
independentes e como integrar esses serviços por meio de componentes de
infraestrutura.

Os principais objetivos são:

-   Implementar uma arquitetura de microserviços.
-   Descobrir e registrar serviços com Eureka.
-   Centralizar o roteamento HTTP com Spring Cloud Gateway.
-   Implementar comunicação síncrona entre serviços.
-   Utilizar RabbitMQ para mensageria assíncrona.
-   Integrar autenticação e autorização com Keycloak.
-   Construir imagens Docker e executar os serviços em containers.
-   Configurar os serviços para se comunicarem por uma rede Docker.

## 🧱 Arquitetura e módulos

  -----------------------------------------------------------------------
  Módulo                              Responsabilidade
  ----------------------------------- -----------------------------------
  `eurekaserver`                      Service Discovery: registro e
                                      descoberta dos microserviços.

  `mscloudgateway`                    API Gateway: ponto de entrada e
                                      roteamento das requisições.

  `mscliente`                         Microserviço responsável pelas
                                      operações de clientes.

  `mscartoes`                         Microserviço responsável pelas
                                      operações de cartões.

  `msavaliadorcredito`                Microserviço de avaliação de
                                      crédito.

  `RabbitMQ`                          Mensageria para comunicação
                                      assíncrona entre componentes que a
                                      utilizam.

  `Keycloak`                          Servidor de identidade para
                                      autenticação e autorização.
  -----------------------------------------------------------------------

A comunicação e as integrações efetivamente utilizadas dependem das
configurações dos módulos. O diagrama abaixo representa a arquitetura
conceitual do projeto.

``` mermaid
flowchart TD
    Cliente[Cliente / Aplicação] --> Gateway[Spring Cloud Gateway]
    Gateway --> MSCliente[mscliente]
    Gateway --> MSCartoes[mscartoes]
    Gateway --> Avaliador[msavaliadorcredito]

    Eureka[Eureka Server] -. registro e descoberta .-> Gateway
    Eureka -. registro e descoberta .-> MSCliente
    Eureka -. registro e descoberta .-> MSCartoes
    Eureka -. registro e descoberta .-> Avaliador

    Keycloak[Keycloak] -. autenticação e autorização .-> Gateway
    RabbitMQ[RabbitMQ] <--> MSCliente
    RabbitMQ <--> MSCartoes
    RabbitMQ <--> Avaliador
```

> O diagrama é ilustrativo: as setas de RabbitMQ e Keycloak indicam
> integrações previstas no ambiente, não necessariamente todas as
> chamadas existentes no código.

## 🛠️ Tecnologias

-   **Java 21 (LTS)**
-   **Spring Boot 3**
-   **Spring Cloud**
-   **Spring Cloud Netflix Eureka**
-   **Spring Cloud Gateway**
-   **Spring Cloud OpenFeign**
-   **RabbitMQ**
-   **Keycloak 18**
-   **Docker**
-   **Maven 3.9.6**

Consulte os `pom.xml` e arquivos de configuração dos módulos para
verificar as versões e dependências efetivamente utilizadas.

## 🗂️ Estrutura do repositório

``` text
micro-services/
├── eurekaserver/
├── msavaliadorcredito/
├── mscartoes/
├── mscliente/
├── mscloudgateway/
└── README.md
```

Cada módulo contém seu código-fonte, configurações Maven e, conforme
compartilhado, um `Dockerfile` para construir sua imagem.

## ⚙️ Pré-requisitos

Instale ou configure:

-   JDK 21.
-   Git.
-   Docker Desktop ou Docker Engine.
-   Maven Wrapper (`mvnw`), quando incluído no módulo.
-   Uma IDE Java, como IntelliJ IDEA, Eclipse ou VS Code.

Conhecimentos recomendados: Java e orientação a objetos, Spring Boot
básico, APIs REST e bancos de dados.

## 🐳 Executando com Docker

Os exemplos a seguir pressupõem que você está na **raiz do repositório**
e que cada diretório de módulo contém seu próprio `Dockerfile`.

### 1. Criar a rede Docker

``` bash
docker network create msnetwork
```

Se a rede já existir, o Docker informará isso; nesse caso, não é
necessário criá-la novamente.

### 2. Construir as imagens

``` bash
docker build -t eurekaserver ./eurekaserver
docker build -t mscloudgateway ./mscloudgateway
docker build -t mscliente ./mscliente
docker build -t mscartoes ./mscartoes
docker build -t msavaliadorcredito ./msavaliadorcredito
```

Os Dockerfiles fornecidos usam uma etapa de build Maven e uma imagem
Java 21 para executar o JAR:

``` dockerfile
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Importante:** esse exemplo de Dockerfile pressupõe que o contexto de
build corresponde a um módulo Maven que gera o JAR em `target/`. Se o
projeto usar um POM pai ou uma estrutura multi-módulo, ajuste o contexto
e o comando de build de acordo com a estrutura real.

### 3. Iniciar os componentes de infraestrutura

Inicie primeiro o Eureka e o RabbitMQ, garantindo que o container do
RabbitMQ tenha o nome esperado pelas variáveis de ambiente usadas nos
microserviços. Em seguida, inicie os serviços de aplicação e o gateway.

Exemplo para o Eureka Server:

``` bash
docker run --name eurekaserver \
  --network msnetwork \
  -p 8761:8761 \
  -d eurekaserver
```

Exemplo para os microserviços:

``` bash
docker run --name mscliente \
  --network msnetwork \
  -e EUREKA_SERVER=eurekaserver \
  -d mscliente

docker run --name mscartoes \
  --network msnetwork \
  -e RABBITMQ_SERVER=cursomsrabbitmq \
  -e EUREKA_SERVER=eurekaserver \
  -d mscartoes

docker run --name msavaliadorcredito \
  --network msnetwork \
  -e RABBITMQ_SERVER=cursomsrabbitmq \
  -e EUREKA_SERVER=eurekaserver \
  -d msavaliadorcredito
```

Os exemplos usam os nomes de variáveis e containers apresentados nas
configurações fornecidas. Confirme se o container RabbitMQ realmente se
chama `cursomsrabbitmq` e se as propriedades Spring leem essas
variáveis.

### 4. Iniciar o Keycloak

Exemplo de execução baseado no comando usado durante o projeto:

``` bash
docker run --name keycloak \
  -p 8081:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  --network msnetwork \
  -d quay.io/keycloak/keycloak:18.0.0 start-dev
```

Acesse o Keycloak pelo navegador em `http://localhost:8081`, quando o
container estiver em execução.

> **Segurança:** `admin/admin` é apenas um exemplo de desenvolvimento.
> Não utilize essas credenciais em produção.

### 5. Iniciar o API Gateway

``` bash
docker run --name mscloudgateway \
  --network msnetwork \
  -p 8080:8080 \
  -e EUREKA_SERVER=eurekaserver \
  -e KEYCLOAK_SERVER=keycloak \
  -e KEYCLOAK_PORT=8080 \
  -d mscloudgateway
```

O gateway fica disponível em `http://localhost:8080`, considerando que a
aplicação esteja configurada para usar a porta `8080`.

### 6. Conferir os containers

``` bash
docker ps
```

Para consultar os logs de um serviço:

``` bash
docker logs -f eurekaserver
docker logs -f mscloudgateway
docker logs -f mscliente
docker logs -f mscartoes
docker logs -f msavaliadorcredito
```

Para remover um container:

``` bash
docker rm -f mscartoes
```

Troque o nome pelo container que deseja remover. A remoção do container
não apaga a imagem Docker.

## 🔧 Observações sobre variáveis de ambiente

Nos Dockerfiles, declarações como:

``` dockerfile
ARG EUREKA_SERVER=localhost
ARG RABBITMQ_SERVER=rabbitmq-host
ARG KEYCLOAK_SERVER=localhost
ARG KEYCLOAK_PORT=8081
```

definem argumentos disponíveis durante a construção da imagem. **Elas
não ficam automaticamente disponíveis como variáveis de ambiente em
tempo de execução.**

Para passar os valores ao iniciar um container, use `docker run -e`,
como nos exemplos acima. A aplicação Spring também precisa estar
configurada para ler essas variáveis, por exemplo, usando placeholders
em `application.yml`.

Dentro da rede Docker, serviços normalmente acessam outros containers
pelo nome do container e pela porta interna do serviço. `localhost`
dentro de um container aponta para o próprio container, não para outro
serviço.

## 🧪 Build local com Maven

No macOS ou Linux, a partir da pasta do módulo:

``` bash
./mvnw clean package
```

No Windows:

``` bat
mvnw.cmd clean package
```

Para executar um módulo localmente, se o Maven Wrapper estiver
disponível:

``` bash
./mvnw spring-boot:run
```

Os Dockerfiles fornecidos utilizam `-DskipTests` para pular os testes
durante a criação da imagem. Para validar o projeto, execute os testes
separadamente sem essa opção.

## 🧠 Conceitos estudados

-   Arquitetura de microserviços.
-   Service Discovery com Eureka.
-   API Gateway com Spring Cloud Gateway.
-   Comunicação síncrona com Spring Cloud OpenFeign.
-   Mensageria e comunicação assíncrona com RabbitMQ.
-   Autenticação e autorização com Keycloak.
-   Balanceamento de carga e descoberta dinâmica.
-   Build de imagens e execução de containers Docker.
-   Configuração de rede e comunicação entre containers.

## 📈 Status

**Projeto finalizado para fins de estudo.**

Este repositório registra a implementação prática dos conceitos de
microserviços. Para uso em produção, seriam necessárias revisões
específicas de segurança, gerenciamento de segredos, observabilidade,
testes automatizados, resiliência e implantação.

## 👨‍💻 Autor

**Gabriel Cabral**\
Projeto desenvolvido como parte dos estudos em Java, Spring Boot, Spring
Cloud e arquitetura de microserviços.

------------------------------------------------------------------------

⭐ Se este projeto foi útil para seus estudos, considere deixar uma
estrela no repositório.
