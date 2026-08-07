import api from "./api";

export function getBoxes() {
  return api.get("/products");
}

export function getBoxById(id) {
  return api.get(`/products/${id}`);
}