FROM openjdk:17

ARG JAR_FILE=/build/libs/galmanhae-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
