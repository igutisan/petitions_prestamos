FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the application JAR from the build output directory
# Make sure you have built the project with `./gradlew build` first
COPY applications/app-service/build/libs/Petitions.jar Petitions.jar

# Set compatible JVM options for OpenJDK
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

# Switch to the non-root user
USER appuser

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar Petitions.jar" ]