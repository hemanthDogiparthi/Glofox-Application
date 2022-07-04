FROM openjdk:8-jdk-alpine
COPY target/StudioApplication-1.0.0.jar StudioApplication-1.0.0.jar
EXPOSE   8080
ENTRYPOINT ["java","-jar","/StudioApplication-1.0.0.jar"]