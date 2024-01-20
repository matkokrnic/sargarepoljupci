
#WORKDIR /APP
#
## Kopiranje izvornog koda u container
##COPY .mvn .mvn
##COPY mvnw .
##COPY pom.xml .
##COPY src src
#
#COPY ./target/your-application.jar /app/your-application.jar
#
#RUN chmod +x mvnw
#
## Pokretanje builda
#RUN apk add --no-cache maven
##RUN ./mvnw clean package
#
## Stvaranje containera u kojem će se vrtiti aplikacija
#FROM openjdk:17-alpine
#
## Kopiranje izvrsnog JAR-a iz build containera u izvrsni container
#COPY --from=builder /*.jar /app.jar
#
## Izlaganje porta
#EXPOSE 8080
#
## Naredba kojom se pokreće aplikacija
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/sargarepoljupci-0.0.1-SNAPSHOT.jar demo.jar
ENTRYPOINT ["java","-jar","demo.jar"]