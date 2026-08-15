package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxPriceRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class SubscriptionBoxPriceRepositoryIntegrationTest {

    @Autowired
    SubscriptionBoxPriceRepository repo;

    @Autowired
    private EntityManager entityManager;


    private SubscriptionBox createSubscriptionBox() {
        var box = new SubscriptionBox();
        box.setName("Monthly Box");
        box.setAvailableUnits(10);
        box.setIsActive(true);

        entityManager.persist(box);
        return box;
    }

    private SubscriptionBoxPrice createPrice(SubscriptionBox box, OffsetDateTime startsAt, OffsetDateTime endsAt) {
        var price = new SubscriptionBoxPrice();

        price.setSubscriptionBox(box);
        price.setAmount(new BigDecimal("29.99"));
        price.setCurrency("CAD");
        price.setStartsAt(startsAt);
        price.setEndsAt(endsAt);
        price.setStripePriceId(UUID.randomUUID().toString());

        entityManager.persist(price);
        return price;
    }

    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.of(
                2026, 8, 2, 23, 38, 39, 657_650_000, ZoneOffset.UTC
        );



    @Test
    void findActivePriceBySubscriptionBoxId_shouldReturnCurrentPrice_whenStartsAtIsBeforeCurrentTime_andEndsAtIsNull() {


        var startsAt = CURRENT_TIME.minusDays(1); // starts Aug 1st 2026
        OffsetDateTime endsAt = null;

        var box = createSubscriptionBox();
        var activePrice = createPrice(box, startsAt, endsAt);

        entityManager.flush();

        var result = repo.findCurrentPrice(box.getId(), CURRENT_TIME);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activePrice.getId());

    }


    @Test
    void findActivePriceBySubscriptionBoxId_shouldReturnCurrentPrice_whenEndAtIsNotNull_andEndsAtIsAfterCurrentTime() {

        var startsAt = CURRENT_TIME.minusDays(1); // starts Aug 1st 2026
        var endsAt = CURRENT_TIME.plusDays(4); // ends August 6th 2026

        var box = createSubscriptionBox();
        var activePrice = createPrice(box, startsAt, endsAt);

        entityManager.flush();

        var result = repo.findCurrentPrice(box.getId(), CURRENT_TIME);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activePrice.getId());

    }

    @Test
    void findCurrentPriceBySubscriptionBoxId_shouldReturnPrice_whenStartsAtEqualsNow() {

        var startsAt = CURRENT_TIME; // starts at current time
        var endsAt = CURRENT_TIME.plusDays(4); // ends August 6th 2026

        var box = createSubscriptionBox();
        var activePrice = createPrice(box, startsAt, endsAt);

        entityManager.flush();

        var result = repo.findCurrentPrice(box.getId(), CURRENT_TIME);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activePrice.getId());
    }

    @Test
    void findCurrentPrice_shouldReturnEmpty_whenEndsAtEqualsNow() {

        var startsAt = CURRENT_TIME.minusDays(1); // starts Aug 1st 2026
        var endsAt = CURRENT_TIME; // ends at current time

        var box = createSubscriptionBox();
        createPrice(box, startsAt, endsAt);

        entityManager.flush();

        var result = repo.findCurrentPrice(box.getId(), CURRENT_TIME);

        assertThat(result).isEmpty();
    }

    @Test
    void findCurrentPriceBySubscriptionBoxId_shouldReturnEmpty_whenPriceStartsInFuture_andEndsAtIsNull() {

        var startsAt = CURRENT_TIME.plusDays(4); // starts Aug 6th 2026
        OffsetDateTime endsAt = null;

        var box = createSubscriptionBox();
        createPrice(box, startsAt, endsAt);

        entityManager.flush();

        var result = repo.findCurrentPrice(box.getId(), CURRENT_TIME);

       assertThat(result).isEmpty();

    }

    @Test
    void findCurrentPrice_shouldReturnEmpty_whenStartsAtAfterCurrentTime_andEndsAtIsNotNull() {

        var startsAt = CURRENT_TIME.plusDays(4); // starts Aug 6th 2026
        var endsAt = CURRENT_TIME.plusDays(10); // ends at Aug 10th 2026

        var box = createSubscriptionBox();
        createPrice(box, startsAt, endsAt);

        entityManager.flush();

        var result = repo.findCurrentPrice(box.getId(), CURRENT_TIME);

        assertThat(result).isEmpty();

    }

    @Test
    void findCurrentPrice_shouldReturnEmpty_whenEndsAtIsBeforeCurrentTime() {

        var startsAt = CURRENT_TIME.minusDays(5); // starts July 28th 2026
        var endsAt = CURRENT_TIME.minusSeconds(1); // ends at Aug 2nd 23:28:38

        var box = createSubscriptionBox();
        createPrice(box, startsAt, endsAt);

        entityManager.flush();

        var result = repo.findCurrentPrice(box.getId(), CURRENT_TIME);

        assertThat(result).isEmpty();
    }

    @Test
    void findCurrentPriceBySubscriptionBoxId_shouldReturnEmpty_whenDifferentSubscriptionBox() {
        var box1 = createSubscriptionBox();
        var box2 = createSubscriptionBox();

        createPrice(
                box2,
                CURRENT_TIME.minusDays(1), // August 1st 2026
                CURRENT_TIME.plusDays(1) // August 3rd 2026
        );

        entityManager.flush();

        var result = repo.findCurrentPrice(
                box1.getId(),
                CURRENT_TIME
        );

        assertThat(result).isEmpty();
    }
}