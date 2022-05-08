FROM adoptopenjdk/openjdk8
ARG JAR_FILE=target/auth-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]