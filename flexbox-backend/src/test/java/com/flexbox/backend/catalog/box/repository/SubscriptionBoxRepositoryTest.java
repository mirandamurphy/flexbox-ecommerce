package com.flexbox.backend.catalog.box.repository;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@Transactional
class SubscriptionBoxRepositoryTest {

    @Autowired
    SubscriptionBoxRepository repo;

    @Autowired
    EntityManager entityManager;

    private SubscriptionBox createBox() {
        var box = new SubscriptionBox();
        box.setName("Yoga Box");
        box.setIsActive(true);
        box.setImageFile("/images/yoga_box.jpg");
        box.setAvailableUnits(10);
        entityManager.persist(box);
        return box;
    }

    @Test
    void updateIsActiveById_shouldSetIsActiveToFalse_whenBoxExists() {
        var box = createBox();
        entityManager.flush();

        var rowsUpdated = repo.updateIsActiveById(false, box.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(rowsUpdated).isEqualTo(1);

        var result = repo.findById(box.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getIsActive()).isFalse();

    }

    @Test
    void updateIsActiveById_shouldReturnZero_whenProductDoesNotExist() {
        var rowsUpdated = repo.updateIsActiveById(false, 999L);

        assertThat(rowsUpdated).isEqualTo(0);

    }

    @Test
    void findById_shouldReturnEmpty_whenBoxDoesNotExist() {

        var result = repo.findById(999L);

        assertThat(result).isEmpty();
    }

}