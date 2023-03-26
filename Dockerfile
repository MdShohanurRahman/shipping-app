FROM openjdk:17-jdk-slim
LABEL mentainer="shohan.drmc@gmail.com"
ARG JAR_FILE=target/middleware-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]