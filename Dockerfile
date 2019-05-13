FROM gradle:jre11-slim as builder

COPY . .
RUN gradle shadowJar

FROM openjdk:11-jre-slim

EXPOSE 1946
COPY --from=builder build/libs/api-1.0-SNAPSHOT.jar api.jar
ENTRYPOINT 'java -jar api.jar'
