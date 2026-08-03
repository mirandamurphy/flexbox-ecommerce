Testcontainers is currently setup with the following workflow when a test runs:
1. Testcontainers starts PostgreSQL
2. Spring Boot (backend) connects using the container's connection details
3. Flyway automatically runs all migrations located in `src/main/resources/db/migration`
4. JPA validates the schema
5. Tests execute against the up-to-date database