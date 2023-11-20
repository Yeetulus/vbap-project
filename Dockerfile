FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/vbapproject.jar /app/vbapproject.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "vbapproject.jar"]