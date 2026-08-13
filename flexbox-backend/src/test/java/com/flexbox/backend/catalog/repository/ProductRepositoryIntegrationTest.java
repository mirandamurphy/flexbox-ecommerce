package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.catalog.product.repository.ProductRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository repo;

    // Tests that @EntityGraph is working
    @Test
    void findById_shouldEagerlyFetchCategory() {
        long id = 9;

        var product = repo.findById(id).orElseThrow();

        assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();
    }
}
