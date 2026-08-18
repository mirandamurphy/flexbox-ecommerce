package com.flexbox.backend.cart;

import com.flexbox.backend.cart.dto.AddCartItemRequest;
import com.flexbox.backend.cart.dto.UpdateCartItemRequest;
import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartItem;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxRepository;
import com.flexbox.backend.user.repository.UserRepository;
import com.flexbox.backend.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CartController.class)
class CartControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    CartService cartService;

    @MockitoBean
    CartRepository cartRepository;

    @MockitoBean
    CartItemRepository cartItemRepository;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    SubscriptionBoxRepository subscriptionBoxRepository;

    private User buildUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Cart buildCart(Long id) {
        Cart cart = new Cart();
        cart.setId(id);
        return cart;
    }

    private SubscriptionBox buildBox(Long id) {
        SubscriptionBox box = new SubscriptionBox();
        box.setId(id);
        box.setName("Test Box");
        return box;
    }

    private CartItem buildCartItem(Long id, SubscriptionBox box, int quantity) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setSubscriptionBox(box);
        item.setQuantity(quantity);
        item.setUnitPriceSnapshot(new BigDecimal("29.99"));
        return item;
    }

    @Test
    void getCart_shouldReturn200_withItemsAndTotal() {
        User user = buildUser(1L);
        Cart cart = buildCart(5L);
        SubscriptionBox box = buildBox(2L);
        CartItem item = buildCartItem(10L, box, 2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartService.getOrCreateActiveCart(user)).thenReturn(cart);
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(item));
        when(cartService.calculateTotal(cart)).thenReturn(new BigDecimal("59.98"));

        var result = mockMvcTester
                .get()
                .uri("/api/cart?userId=1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .extractingPath("$.total")
                .isEqualTo(59.98);

        assertThat(result)
                .bodyJson()
                .extractingPath("$.items[0].subscriptionBoxName")
                .isEqualTo("Test Box");
    }

    @Test
    void getCart_shouldReturn404_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/cart?userId=99")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void addItem_shouldReturn404_whenSubscriptionBoxNotFound() {
        User user = buildUser(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionBoxRepository.findById(999L)).thenReturn(Optional.empty());

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/cart/items?userId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"subscriptionBoxId\":999,\"quantity\":1}")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void addItem_shouldReturn200_whenValid() {
        User user = buildUser(1L);
        SubscriptionBox box = buildBox(2L);
        Cart cart = buildCart(5L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionBoxRepository.findById(2L)).thenReturn(Optional.of(box));
        when(cartService.getOrCreateActiveCart(user)).thenReturn(cart);
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of());
        when(cartService.calculateTotal(cart)).thenReturn(BigDecimal.ZERO);

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/cart/items?userId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"subscriptionBoxId\":2,\"quantity\":3}")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.OK);
    }

    @Test
    void updateQuantity_shouldReturn200_withUpdatedItem() {
        SubscriptionBox box = buildBox(2L);
        CartItem updated = buildCartItem(10L, box, 5);

        when(cartService.updateQuantity(10L, 5)).thenReturn(updated);

        MvcTestResult result = mockMvcTester
                .patch()
                .uri("/api/cart/items/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\":5}")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .extractingPath("$.quantity")
                .isEqualTo(5);
    }

    @Test
    void updateQuantity_shouldReturn400_whenQuantityInvalid() {
        when(cartService.updateQuantity(10L, 0))
                .thenThrow(new InvalidQuantityException("Quantity must be greater than zero, use removeItem to delete a cart item"));

        MvcTestResult result = mockMvcTester
                .patch()
                .uri("/api/cart/items/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\":0}")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void removeItem_shouldReturn204() {
        MvcTestResult result = mockMvcTester
                .delete()
                .uri("/api/cart/items/10")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }
}
