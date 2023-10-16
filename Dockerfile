FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ENTRYPOINT ["java","-jar","build/libs/AwsIpRangesAsync-0.0.1-SNAPSHOT.jar"]