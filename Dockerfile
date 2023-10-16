FROM openjdk:17-jdk-alpine
ARG JAR_FILE=./build/libs/app.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-jar","/app.jar"]