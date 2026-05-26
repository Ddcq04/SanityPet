# Paso 1: Usar una imagen de Maven para compilar el proyecto
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Usamos el nombre exacto de tu archivo JAR que acabamos de ver
COPY target/gestion-clinica-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "app.jar"]