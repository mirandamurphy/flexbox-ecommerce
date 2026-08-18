import api from "./api";

export function getBoxes() {
  return api.get("/catalog/boxes");
}

export function getBoxById(id) {
  return api.get(`/catalog/boxes/${id}`);
}