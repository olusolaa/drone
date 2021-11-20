FROM openjdk:11-jdk-slim

WORKDIR /app

COPY src/main/java/musala /app

ENTRYPOINT [ "./mvnw", "spring-boot:run" ]