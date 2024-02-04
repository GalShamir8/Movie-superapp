FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM openjdk:17-jdk-slim
EXPOSE 8085
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
COPY src/ src/

ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]