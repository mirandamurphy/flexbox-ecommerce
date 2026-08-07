import { createContext, useContext, useEffect, useMemo, useState } from "react";

import { loginUser, registerUser } from "../services/authService";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("flexboxUser");

    if (savedUser) {
      setCurrentUser(JSON.parse(savedUser));
    }
  }, []);

  async function login(email, password) {
    if (!email || !password) {
      throw new Error("Email and password are required.");
    }

    const response = await loginUser({ email, password });
    const { userId, email: returnedEmail, token } = response.data;

    const user = {
      id: userId,
      email: returnedEmail,
    };

    localStorage.setItem("flexboxUser", JSON.stringify(user));
    localStorage.setItem("flexboxToken", token);

    setCurrentUser(user);

    return user;
  }

  async function register({ name, email, password }) {
    if (!name || !email || !password) {
      throw new Error("All fields are required.");
    }

    const [firstName, ...rest] = name.trim().split(" ");
    const lastName = rest.join(" ") || firstName;

    const response = await registerUser({
      firstName,
      lastName,
      email,
      password,
    });
    const { userId, email: returnedEmail, token } = response.data;

    const user = {
      id: userId,
      email: returnedEmail,
    };

    localStorage.setItem("flexboxUser", JSON.stringify(user));
    localStorage.setItem("flexboxToken", token);

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
