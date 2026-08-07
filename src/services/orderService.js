import api from "./api";

export function getOrders() {
  return api.get("/orders");
}

export function getOrderById(id) {
  return api.get(`/orders/${id}`);
}