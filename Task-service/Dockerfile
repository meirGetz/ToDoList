FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY target/Task-service-0.0.1-SNAPSHOT.jar task-service.jar

FROM openjdk:17-jdk-slim
COPY --from=build /app/task-service.jar /usr/app/
WORKDIR /usr/app
CMD ["java", "-jar", "task-service.jar"]

EXPOSE 8082