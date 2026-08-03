package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.catalog.entity.Category;
import com.flexbox.backend.catalog.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import com.flexbox.backend.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import (TestcontainersConfiguration.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findProductById_shouldEagerlyFetchCategory() {
        var category = new Category();
        category.setName("Protein");
        entityManager.persist(category);

        var product = new Product();
        product.setBrand("ProteinPowders");
        product.setName("Caramel Protein Powder");
        product.setDescription("Caramel flavored protein powder with 30g of protein per pack.");
        product.setCategory(category);
        product.setCostPerUnit(new BigDecimal(1.20));
        entityManager.persist(product);

        entityManager.flush();
        entityManager.clear();

        Optional<Product> result = productRepository.findById(product.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getCategory().getName()).isEqualTo("Protein");
    }



}