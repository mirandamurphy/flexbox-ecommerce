package com.flexbox.backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer postgresContainer() {
        // Keeping the simpler default-superuser setup for now, proven working
        // against all existing tests. A more production-realistic version
        // exists (mounting the real role-creation init scripts), worth
        // adopting for A4 once there is time to verify it end to end.
        return new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withUrlParam("stringtype", "unspecified");
    }
}
