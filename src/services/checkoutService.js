import api from "./api";

// userId is passed explicitly for now, same reason as in cartService.js.

export function createCheckoutSession(userId) {
  return api.post("/checkout", null, { params: { userId } });
}

export function retryCheckout(orderId) {
  return api.post(`/checkout/${orderId}/retry`);
}
