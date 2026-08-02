import api from "./api";

export function loginUser(credentials) {
  return api.post("/auth/login", credentials);
}

export function registerUser(userData) {
  return api.post("/auth/register", userData);
}