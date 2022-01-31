#syntax=docker/dockerfile

FROM maven:3.8-openjdk-17-slim as prepare
WORKDIR /workspace
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src

#test stage
FROM prepare AS test
RUN mvn test

#build stage
FROM test AS build
RUN mvn package -DskipTests
RUN mkdir target/extracted \
    && java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted


FROM openjdk:17-slim AS final
USER 1000
WORKDIR /app
ARG EXTRACTED=/workspace/target/extracted
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]