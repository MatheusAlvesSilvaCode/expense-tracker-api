# Etapa 1: Build do JAR com Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia as dependências primeiro para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e gera o pacote
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem leve de execução (JRE)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]