 FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 24030
ENTRYPOINT ["java", "-jar","/app.jar"]