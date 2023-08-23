FROM maven:3.6.3-jdk-11-slim AS build
LABEL authors="isasayar"
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml -Dmaven.test.skip=true clean package

FROM amazoncorretto:11-alpine
COPY --from=build /usr/src/app/target/user-service.jar /usr/app/user-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/usr/app/user-service.jar"]