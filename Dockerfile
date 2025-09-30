# --- Etapa 1: Construcción del JAR ---
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# ✨ PRIMERO copiamos solo el pom.xml (esto se cachea)
COPY pom.xml .

# ✨ Descargamos las dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# DESPUÉS copiamos el código fuente
COPY src ./src

# Compilamos (más rápido porque las deps ya están cacheadas)
RUN mvn clean package -Dmaven.test.skip=true

# --- Etapa 2: Imagen final ligera ---
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone Spring Boot
EXPOSE 8080

# ✨ Permite pasar opciones JVM si es necesario
ENV JAVA_OPTS=""

# Comando de arranque con opciones JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]