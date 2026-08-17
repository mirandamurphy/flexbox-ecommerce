# Build
FROM eclipse-temurin:25 AS build
WORKDIR /app

# Dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source code and compile
# -DskipTest is added because tests use Testcontainers
COPY src ./src
RUN ./mvnw clean package -DskipTest

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
