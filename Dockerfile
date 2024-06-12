FROM gradle:7.4.2-jdk17 AS build

WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradlew /app/
COPY gradle /app/gradle
COPY src /app/src
RUN ./gradlew build -x test --no-daemon

FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080


CMD ["java", "-jar", "app.jar"]