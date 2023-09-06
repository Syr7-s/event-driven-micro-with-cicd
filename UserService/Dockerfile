FROM maven:3.6.3-jdk-11-slim AS build
LABEL authors="isasayar"
COPY . /usr/src/app
COPY core/ /usr/src
#RUN cp -Rf ./core /usr/src
RUN mvn  -f /usr/src/app/pom.xml  -Dmaven.test.skip=true clean package

FROM amazoncorretto:11-alpine
COPY --from=build /usr/src/app/target/user-service.jar /usr/app/user-service.jar

ENTRYPOINT ["java","-jar","/usr/app/user-service.jar"]






