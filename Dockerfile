
FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build ./build/target/*.jar ./gerenciadorTarefa.jar
ENTRYPOINT java -jar gerenciadorTarefa.jar

