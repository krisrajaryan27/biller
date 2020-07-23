FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/biller.jar
COPY ${JAR_FILE} biller.jar
ENTRYPOINT ["java","-jar","biller.jar"]