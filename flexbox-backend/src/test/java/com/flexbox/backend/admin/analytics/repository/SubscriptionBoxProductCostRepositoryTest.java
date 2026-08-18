package com.flexbox.backend.admin.analytics.repository;

import com.flexbox.backend.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestcontainersConfiguration.class)
class SubscriptionBoxProductCostRepositoryTest {

    @Autowired
    private SubscriptionBoxProductCostRepository repository;

    @Test
    void findByIdSubscriptionBoxId_shouldReturnProductsForBox() {

        var result = repository.findById_SubscriptionBoxId(1L);

        assertThat(result)
                .isNotEmpty();

        assertThat(result)
                .allMatch(product ->
                        product.getId().getSubscriptionBoxId().equals(1L));
    }
}