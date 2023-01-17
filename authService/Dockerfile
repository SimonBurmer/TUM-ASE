FROM maven:3.8-openjdk-18 as builder

WORKDIR /usr/app

COPY backendParent/pom.xml pom.xml
COPY backendCommon backendCommon

COPY authService/src authService/src
COPY authService/pom.xml authService

RUN ls -alh authService/

RUN mvn clean package -P auth-service-profile -pl :auth-service -DskipTests -am


FROM openjdk:18
COPY --from=builder target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar
ENTRYPOINT ["java", "-jar","auth-service.jar"]
EXPOSE 8080