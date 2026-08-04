package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.catalog.model.Category;
import com.flexbox.backend.catalog.model.Product;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    // Tests that @EntityGraph is working
    @Test
    void findById_shouldEagerlyFetchCategory() {
        long id = 9;

        var product = productRepository.findById(id).orElseThrow();

        assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();
    }
}
