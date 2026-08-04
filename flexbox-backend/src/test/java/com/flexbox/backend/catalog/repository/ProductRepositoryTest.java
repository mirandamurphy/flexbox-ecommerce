package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.catalog.model.Category;
import com.flexbox.backend.catalog.model.Product;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findProductById_shouldEagerlyFetchCategory() {
        var category = new Category();
        category.setName("Protein");
        categoryRepository.save(category);

        var product = new Product();
        product.setSku("PP-CP-001");
        product.setBrand("ProteinPowders");
        product.setName("Caramel Protein Powder");
        product.setDescription("Caramel flavored protein powder with 30g of protein per pack.");
        product.setCategory(category);
        product.setCostPerUnit(new BigDecimal("1.20"));
        productRepository.save(product);

        Optional<Product> result = productRepository.findById(product.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getCategory().getName()).isEqualTo("Protein");
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        var category = new Category();
        category.setName("Vitamins");
        categoryRepository.save(category);

        var product1 = new Product();
        product1.setBrand("VitaBrand");
        product1.setName("Vitamin C 1000mg");
        product1.setDescription("High-dose vitamin C tablets.");
        product1.setCategory(category);
        product1.setCostPerUnit(new BigDecimal("0.50"));
        productRepository.save(product1);

        var product2 = new Product();
        product2.setBrand("VitaBrand");
        product2.setName("Vitamin D3 2000IU");
        product2.setDescription("Vitamin D3 softgels.");
        product2.setCategory(category);
        product2.setCostPerUnit(new BigDecimal("0.75"));
        productRepository.save(product2);

        List<Product> result = productRepository.findAll();

        assertThat(result).hasSize(2);
    }
}
