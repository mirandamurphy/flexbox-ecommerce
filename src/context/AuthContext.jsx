import { createContext, useContext, useEffect, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("flexboxUser");

    if (savedUser) {
      setCurrentUser(JSON.parse(savedUser));
    }
  }, []);

  function login(email, password) {
    if (!email || !password) {
      throw new Error("Email and password are required.");
    }

    const user = {
      id: 1,
      name: "Demo Customer",
      email,
      role: email.includes("admin") ? "ADMIN" : "CUSTOMER",
    };

    localStorage.setItem("flexboxUser", JSON.stringify(user));
    localStorage.setItem("flexboxToken", "mock-jwt-token");

    setCurrentUser(user);

    return user;
  }

  function register({ name, email, password }) {
    if (!name || !email || !password) {
      throw new Error("All fields are required.");
    }

    const user = {
      id: Date.now(),
      name,
      email,
      role: "CUSTOMER",
    };

    localStorage.setItem("flexboxUser", JSON.stringify(user));
    localStorage.setItem("flexboxToken", "mock-jwt-token");

    setCurrentUser(user);

    return user;
  }

  function logout() {
    localStorage.removeItem("flexboxUser");
    localStorage.removeItem("flexboxToken");
    setCurrentUser(null);
  }

  const value = useMemo(
    () => ({
      currentUser,
      isAuthenticated: Boolean(currentUser),
      login,
      register,
      logout,
    }),
    [currentUser]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider.");
  }

  return context;
}