FROM gradle:7.5.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build unpack --no-daemon --stacktrace

FROM openjdk:17-alpine
ARG DEPENDENCY=/home/gradle/src/build/docker/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 80 443
ENTRYPOINT ["java","-cp","app:app/lib/*","com.qurlapi.qurlapi.QUrlApiApplication"]