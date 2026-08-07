import { createContext, useContext, useEffect, useMemo, useState } from "react";

import {
  addItemToCart,
  getCart,
  removeCartItem,
  updateCartItemQuantity,
} from "../services/cartService";
import { useAuth } from "./AuthContext";

const CartContext = createContext(null);

function toDisplayItem(apiItem) {
  return {
    id: apiItem.id,
    subscriptionBoxId: apiItem.subscriptionBoxId,
    name: apiItem.subscriptionBoxName,
    price: apiItem.unitPrice,
    quantity: apiItem.quantity,
  };
}

export function CartProvider({ children }) {
  const { currentUser } = useAuth();
  const [cartItems, setCartItems] = useState([]);

  async function refreshCart() {
    if (!currentUser) {
      setCartItems([]);
      return;
    }

    const response = await getCart(currentUser.id);
    setCartItems(response.data.items.map(toDisplayItem));
  }

  useEffect(() => {
    refreshCart();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentUser]);

  async function addToCart(box) {
    if (!currentUser) {
      return;
    }

    await addItemToCart(currentUser.id, box.id, 1);
    await refreshCart();
  }

  async function removeFromCart(cartItemId) {
    await removeCartItem(cartItemId);
    await refreshCart();
  }

  async function updateQuantity(cartItemId, quantity) {
    const safeQuantity = Number(quantity);

    if (safeQuantity <= 0) {
      await removeFromCart(cartItemId);
      return;
    }

    await updateCartItemQuantity(cartItemId, safeQuantity);
    await refreshCart();
  }

  function clearCart() {
    // No bulk clear endpoint exists yet. This resets the visible cart
    // client side, used after a checkout redirect where the user is
    // about to leave the app for Stripe anyway.
    setCartItems([]);
  }

  const cartCount = useMemo(
    () =>
      cartItems.reduce(
        (total, item) => total + item.quantity,
        0
      ),
    [cartItems]
  );

  const cartTotal = useMemo(
    () =>
      cartItems.reduce(
        (total, item) => total + item.price * item.quantity,
        0
      ),
    [cartItems]
  );

  const value = {
    cartItems,
    cartCount,
    cartTotal,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);

  if (!context) {
    throw new Error("useCart must be used inside CartProvider.");
  }

  return context;
}
