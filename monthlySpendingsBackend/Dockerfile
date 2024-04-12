FROM --platform=linux/arm64 openjdk:17
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT java -jar app.jar