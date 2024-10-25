FROM openjdk:17-slim
WORKDIR /app

COPY target/BlogSystem-0.0.1-SNAPSHOT.jar ./myapp.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "myapp.jar"]
