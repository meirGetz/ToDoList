FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY target/User-service-0.0.1-SNAPSHOT.jar user-service.jar

# שלב הריצה
FROM openjdk:17-jdk-slim
COPY --from=build /app/user-service.jar /usr/app/
WORKDIR /usr/app
CMD ["java", "-jar", "user-service.jar"]

EXPOSE 8081