package com.flexbox.backend.catalog.product.repository;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.catalog.product.model.Category;
import com.flexbox.backend.catalog.product.model.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestcontainersConfiguration.class)
@Transactional
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private EntityManager entityManager;

    private Category createCategory(String name) {
        var category = new Category();
        category.setName(name);
        entityManager.persist(category);
        return category;
    }

    private Product createProduct(Category category, String sku, boolean isActive) {
        var product = new Product();
        product.setName("Sunscreen");
        product.setSku(sku);
        product.setCostPerUnit(BigDecimal.valueOf(1.02));
        product.setCategory(category);
        product.setIsActive(isActive);
        entityManager.persist(product);
        return product;
    }

    @Test
    void updateIsActiveById_shouldSetIsActiveToFalse_whenProductExists() {

        var category = createCategory("water bottles");
        var product = createProduct(category, "SKU-123",  true);
        entityManager.flush();

        var rowsUpdated = repo.updateIsActiveById(false, product.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(rowsUpdated).isEqualTo(1);

        var result = repo.findById(product.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getIsActive()).isFalse();
    }


    @Test
    void updateIsActiveById_shouldReturnZero_whenProductDoesNotExist() {
        var rowsUpdated = repo.updateIsActiveById(false, 999L);

        assertThat(rowsUpdated).isEqualTo(0);

    }

    @Test
    void findById_shouldReturnProduct_andEagerlyFetchCategory() {

        var category = createCategory("headbands");

        var product = createProduct(category,"SKU-124", true);

        entityManager.flush();
        entityManager.clear();

        var result = repo.findById(product.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getCategory()).isNotNull();
        assertThat(result.get().getCategory().getName()).isEqualTo("headbands");

    }

    @Test
    void findById_shouldReturnEmpty_whenProductDoesNotExist() {

        var result = repo.findById(999L);

        assertThat(result).isEmpty();
    }
}
