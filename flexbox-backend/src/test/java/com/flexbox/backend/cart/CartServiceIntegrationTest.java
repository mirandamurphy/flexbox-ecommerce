package com.flexbox.backend.cart;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartItem;
import com.flexbox.backend.cart.model.CartStatus;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User testUser;
    private SubscriptionBox testBox;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("cart-test@flexbox.ca");
        testUser.setPasswordHash("dummy-hash");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPhoneNumber("1234567890");
        testUser.setIsEnabled(true);
        entityManager.persist(testUser);

        testBox = new SubscriptionBox();
        testBox.setName("Beginner Fitness Box");
        testBox.setDescription("Starter box for the test suite");
        testBox.setIsActive(true);
        testBox.setAvailableUnits(50);
        entityManager.persist(testBox);

        SubscriptionBoxPrice price = new SubscriptionBoxPrice();
        price.setSubscriptionBox(testBox);
        price.setAmount(new BigDecimal("29.99"));
        price.setCurrency("CAD");
        price.setStartsAt(OffsetDateTime.now().minusDays(1));
        price.setEndsAt(null);
        price.setStripePriceId("price_test_" + System.nanoTime());
        entityManager.persist(price);

        entityManager.flush();
    }

    @Test
    void addItem_createsNewCartAndCartItem() {
        CartItem item = cartService.addItem(testUser, testBox, 2);

        assertThat(item.getId()).isNotNull();
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getUnitPriceSnapshot()).isEqualByComparingTo("29.99");

        Cart cart = cartRepository.findByUserAndStatus(testUser, CartStatus.ACTIVE).orElseThrow();
        assertThat(cart.getId()).isNotNull();
    }

    @Test
    void addItem_calledTwice_mergesQuantityInsteadOfDuplicating() {
        cartService.addItem(testUser, testBox, 2);
        CartItem merged = cartService.addItem(testUser, testBox, 3);

        assertThat(merged.getQuantity()).isEqualTo(5);

        Cart cart = cartRepository.findByUserAndStatus(testUser, CartStatus.ACTIVE).orElseThrow();
        assertThat(cartItemRepository.findByCart(cart)).hasSize(1);
    }

    @Test
    void calculateTotal_reflectsQuantityTimesUnitPrice() {
        cartService.addItem(testUser, testBox, 3);
        Cart cart = cartRepository.findByUserAndStatus(testUser, CartStatus.ACTIVE).orElseThrow();

        BigDecimal total = cartService.calculateTotal(cart);

        assertThat(total).isEqualByComparingTo("89.97");
    }

    @Test
    void removeItem_deletesTheCartItem() {
        CartItem item = cartService.addItem(testUser, testBox, 1);

        cartService.removeItem(item.getId());

        assertThat(cartItemRepository.findById(item.getId())).isEmpty();
    }

    @Test
    void updateQuantity_changesTheStoredQuantity() {
        CartItem item = cartService.addItem(testUser, testBox, 1);

        cartService.updateQuantity(item.getId(), 7);

        CartItem updated = cartItemRepository.findById(item.getId()).orElseThrow();
        assertThat(updated.getQuantity()).isEqualTo(7);
    }

    @Test
    void addItem_exceedingAvailableStock_throwsInsufficientStockException() {
        testBox.setAvailableUnits(3);

        assertThatThrownBy(() -> cartService.addItem(testUser, testBox, 5))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("3");
    }

    @Test
    void addItem_calledTwiceExceedingStockOnSecondCall_throwsInsufficientStockException() {
        testBox.setAvailableUnits(4);

        cartService.addItem(testUser, testBox, 3);

        assertThatThrownBy(() -> cartService.addItem(testUser, testBox, 2))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void updateQuantity_toZeroOrLess_throwsInvalidQuantityException() {
        CartItem item = cartService.addItem(testUser, testBox, 2);

        assertThatThrownBy(() -> cartService.updateQuantity(item.getId(), 0))
                .isInstanceOf(InvalidQuantityException.class);

        assertThatThrownBy(() -> cartService.updateQuantity(item.getId(), -3))
                .isInstanceOf(InvalidQuantityException.class);

        CartItem unchanged = cartItemRepository.findById(item.getId()).orElseThrow();
        assertThat(unchanged.getQuantity()).isEqualTo(2);
    }

    @Test
    void updateQuantity_exceedingAvailableStock_throwsInsufficientStockException() {
        testBox.setAvailableUnits(5);
        CartItem item = cartService.addItem(testUser, testBox, 2);

        assertThatThrownBy(() -> cartService.updateQuantity(item.getId(), 10))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("5");

        CartItem unchanged = cartItemRepository.findById(item.getId()).orElseThrow();
        assertThat(unchanged.getQuantity()).isEqualTo(2);
    }
}
