# Build
FROM eclipse-temurin:25 AS build
WORKDIR /app

# Dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source code and compile
# -DskipTests is added because tests use Testcontainers, which needs a
# Docker socket that is not available inside the image build step itself
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
