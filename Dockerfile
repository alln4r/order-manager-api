FROM openjdk:8-jdk
COPY target/ORDER-MANAGER-jar-with-dependencies.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
