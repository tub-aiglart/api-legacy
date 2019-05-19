FROM gradle:jre11-slim as builder
WORKDIR /etc/tub-api
COPY ./ ./
USER root
RUN gradle shadowJar

FROM openjdk:11-jre-slim

EXPOSE 1946
COPY --from=builder ./etc/tub-api/build/libs/api-1.0-SNAPSHOT.jar api.jar
ENTRYPOINT 'java -jar api.jar'
