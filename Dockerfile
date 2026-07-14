# ================================
# ETAPA 1: BUILD
# ================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# copiamos primero solo los archivos de dependencias
# para aprovechar el cache de Docker (si no cambian las deps, no re-descarga)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# damos permisos al gradlew
RUN chmod +x gradlew

# descargamos dependencias primero (cacheado por Docker)
RUN ./gradlew dependencies --no-daemon

# copiamos el código fuente
COPY src src

# compilamos y generamos el JAR
RUN ./gradlew bootJar --no-daemon

# ================================
# ETAPA 2: RUNTIME
# ================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# copiamos solo el JAR de la etapa de build
COPY --from=builder /app/build/libs/*.jar app.jar

# puerto que expone la app
EXPOSE 8080

# variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "app.jar"]