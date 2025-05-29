FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY *.jar EntreBicisAnna.jar
ENTRYPOINT ["java","-jar","/EntreBicisAnna.jar"]