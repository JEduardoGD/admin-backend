FROM alpine/java:21-jdk
COPY target/Register-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
