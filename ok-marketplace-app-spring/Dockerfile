FROM bellsoft/liberica-openjdk-alpine:17
COPY build/libs/ok-marketplace-app-spring-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]
