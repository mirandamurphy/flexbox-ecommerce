import { Link } from "react-router-dom";

import { useCart } from "../context/CartContext";

function CartPage() {
  const {
    cartItems,
    cartTotal,
    removeFromCart,
    updateQuantity,
  } = useCart();

  if (cartItems.length === 0) {
    return (
      <main className="page">
        <h1>Shopping Cart</h1>
        <p>Your cart is currently empty.</p>

        <Link className="primary-button" to="/boxes">
          Browse Subscription Boxes
        </Link>
      </main>
    );
  }

  return (
    <main className="page">
      <h1>Shopping Cart</h1>

      <section className="cart-list">
        {cartItems.map((item) => (
          <article className="cart-item" key={item.id}>
            <div>
              <h2>{item.name}</h2>
              <p>${item.price.toFixed(2)} each</p>
            </div>

            <label>
              Quantity
              <input
                type="number"
                min="1"
                max={item.stock}
                value={item.quantity}
                onChange={(event) =>
                  updateQuantity(
                    item.id,
                    event.target.value
                  )
                }
              />
            </label>

            <p>
              ${(item.price * item.quantity).toFixed(2)}
            </p>

            <button
              type="button"
              onClick={() => removeFromCart(item.id)}
            >
              Remove
            </button>
          </article>
        ))}
      </section>

      <section className="cart-summary">
        <h2>Total: ${cartTotal.toFixed(2)}</h2>

        <Link className="primary-button" to="/checkout">
          Proceed to Checkout
        </Link>
      </section>
    </main>
  );
}

export default CartPage;