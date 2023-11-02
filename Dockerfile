FROM eclipse-temurin:17-jdk-alpine
VOLUME /build
COPY target/*.jar Mercadona-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Mercadona-0.0.1-SNAPSHOT.jar"]