# Container za izgradnju (build) aplikacije
FROM openjdk:17-alpine AS builder

WORKDIR /PROGI_PROJEKT/Izvornikod/backend

# Kopiranje izvornog koda u container
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src

RUN chmod +x mvnw

# Pokretanje builda
RUN apk add --no-cache maven
#RUN ./mvnw clean package

# Stvaranje containera u kojem će se vrtiti aplikacija
FROM openjdk:17-alpine

# Kopiranje izvrsnog JAR-a iz build containera u izvrsni container
COPY --from=builder /*.jar /app.jar

# Izlaganje porta
EXPOSE 8080

# Naredba kojom se pokreće aplikacija
ENTRYPOINT ["java","-jar","/app.jar"]