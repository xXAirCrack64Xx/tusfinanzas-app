FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY mvnw* pom.xml ./
COPY src ./src
RUN ./mvnw package -Dquarkus.package.jar.type=uber-jar

FROM eclipse-temurin:21-jre
WORKDIR /work/
COPY --from=build /app/target/*-runner.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
