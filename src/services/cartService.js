import api from "./api";

// userId is passed explicitly for now instead of being read from the
// logged in user automatically. The backend does not have its JWT
// security filter wired up yet, so there is no way to identify the
// current user from the request alone. Once that is done, these calls
// can drop the userId parameter.

export function getCart(userId) {
  return api.get("/cart", { params: { userId } });
}

export function addItemToCart(userId, subscriptionBoxId, quantity) {
  return api.post(
    "/cart/items",
    { subscriptionBoxId, quantity },
    { params: { userId } }
  );
}

export function updateCartItemQuantity(cartItemId, quantity) {
  return api.patch(`/cart/items/${cartItemId}`, { quantity });
}

export function removeCartItem(cartItemId) {
  return api.delete(`/cart/items/${cartItemId}`);
}
