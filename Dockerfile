FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/subscribers.jar /app

EXPOSE 8080

CMD ["java", "-jar", "subscribers.jar"]