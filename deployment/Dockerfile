FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
# Copia el JAR generado por Gradle
COPY applications/app-service/build/libs/*.jar app.jar
# Extrae las capas para el cache de Docker
RUN java -Djarmode=layertools -jar app.jar extract

# --- Segunda Etapa: Imagen Final ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Crea un usuario y grupo no-root para mayor seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Crea directorios necesarios y asigna permisos
RUN mkdir /cache && \
    chown -R appuser:appgroup /app && \
    chown -R appuser:appgroup /cache

# Cambia al usuario no-root
USER appuser

# Copia las capas extraídas desde la etapa builder
# Estas capas se cachearán de forma independiente
COPY --from=builder /app/dependencies/ ./ 
COPY --from=builder /app/spring-boot-loader/ ./ 
COPY --from=builder /app/snapshot-dependencies/ ./ 
COPY --from=builder /app/application/ ./ 

# Punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=70", "org.springframework.boot.loader.launch.JarLauncher"]
