package com.flexbox.backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:18")
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("init-scripts/01_create_roles_app.sh"),
                        "/docker-entrypoint-initdb.d/01_create_roles_app.sh"
                )
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("init-scripts/02_grant_privileges_app.sh"),
                        "/docker-entrypoint-initdb.d/02_grant_privileges_app.sh"
                );
    }
}
