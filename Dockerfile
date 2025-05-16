FROM openjdk:21-jdk-slim

# Arbeitsverzeichnis im Container
WORKDIR /usr/src/app

# Maven Wrapper + pom.xml + source code kopieren
COPY .mvn .mvn
COPY models models
COPY pom.xml mvnw ./
COPY src src

# CRLF entfernen, falls du auf Windows arbeitest
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

# App bauen
RUN ./mvnw -Dmaven.test.skip=true package

# Port konfigurieren
EXPOSE 8080

# App starten
CMD ["java", "-jar", "/usr/src/app/target/app.jar"]
