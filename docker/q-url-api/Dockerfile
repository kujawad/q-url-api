FROM openjdk:11-jre-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /q-url-api.jar
ENTRYPOINT ["java","-jar","/q-url-api.jar"]