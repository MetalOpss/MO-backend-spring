# --- Etapa 1: Construcción del JAR ---
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos todo el proyecto (pom.xml + src + etc.)
COPY . .

# Compilamos y empaquetamos (saltamos tests para más rápido)
RUN mvn clean package -DskipTests

# --- Etapa 2: Imagen final ligera ---
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone Spring Boot
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
