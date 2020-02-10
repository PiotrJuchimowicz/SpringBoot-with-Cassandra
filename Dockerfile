FROM openjdk:8
ADD target/messaging-app.jar  messaging-app.jar
ENTRYPOINT ["java", "-jar", "messaging-app.jar"]
EXPOSE 8080 587
