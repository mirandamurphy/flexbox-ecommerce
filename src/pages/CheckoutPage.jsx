import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";
import { createCheckoutSession } from "../services/checkoutService";

function CheckoutPage() {
  const navigate = useNavigate();
  const { currentUser } = useAuth();
  const { cartItems, cartTotal } = useCart();

  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    address: "",
    city: "",
    province: "",
    postalCode: "",
    cardholderName: "",
    cardNumber: "",
  });

  const [isProcessing, setIsProcessing] = useState(false);
  const [error, setError] = useState("");

  function handleChange(event) {
    const { name, value } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    if (cartItems.length === 0) {
      setError("Your cart is empty.");
      return;
    }

    const hasEmptyField = Object.values(formData).some(
      (value) => value.trim() === ""
    );

    if (hasEmptyField) {
      setError("Please complete all required fields.");
      return;
    }

    setIsProcessing(true);

    try {
      const response = await createCheckoutSession(currentUser.id);
      // Redirects to Stripe's hosted checkout page. The card fields
      // above are not sent anywhere, Stripe collects real payment
      // details on its own page, not through this form.
      window.location.href = response.data.checkoutUrl;
    } catch (checkoutError) {
      const message =
        checkoutError.response?.data?.detail || checkoutError.message;
      setError(message);
      setIsProcessing(false);
    }
  }

  if (cartItems.length === 0) {
    return (
      <main className="page">
        <h1>Checkout</h1>
        <p>Your cart is empty.</p>
      </main>
    );
  }

  return (
    <main className="page">
      <h1>Checkout</h1>

      <div className="checkout-layout">
        <form className="checkout-form" onSubmit={handleSubmit}>
          <section className="form-section">
            <h2>Contact Information</h2>

            <label>
              Full Name
              <input
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                required
              />
            </label>

            <label>
              Email
              <input
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </label>
          </section>

          <section className="form-section">
            <h2>Shipping Address</h2>

            <label>
              Address
              <input
                name="address"
                value={formData.address}
                onChange={handleChange}
                required
              />
            </label>

            <label>
              City
              <input
                name="city"
                value={formData.city}
                onChange={handleChange}
                required
              />
            </label>

            <label>
              Province
              <select
                name="province"
                value={formData.province}
                onChange={handleChange}
                required
              >
                <option value="">Select province</option>
                <option value="ON">Ontario</option>
                <option value="QC">Quebec</option>
                <option value="BC">British Columbia</option>
                <option value="AB">Alberta</option>
              </select>
            </label>

            <label>
              Postal Code
              <input
                name="postalCode"
                value={formData.postalCode}
                onChange={handleChange}
                placeholder="A1A 1A1"
                required
              />
            </label>
          </section>

          <section className="form-section">
            <h2>Payment</h2>

            <p className="form-note">
              Demo payment form. Stripe integration will replace
              these fields.
            </p>

            <label>
              Cardholder Name
              <input
                name="cardholderName"
                value={formData.cardholderName}
                onChange={handleChange}
                required
              />
            </label>

            <label>
              Test Card Number
              <input
                name="cardNumber"
                value={formData.cardNumber}
                onChange={handleChange}
                placeholder="4242 4242 4242 4242"
                required
              />
            </label>
          </section>

          {error && <p className="error-message">{error}</p>}

          <button
            className="primary-button checkout-button"
            type="submit"
            disabled={isProcessing}
          >
            {isProcessing
              ? "Processing Payment..."
              : `Place Order — $${cartTotal.toFixed(2)}`}
          </button>
        </form>

        <aside className="order-summary">
          <h2>Order Summary</h2>

          {cartItems.map((item) => (
            <div className="summary-item" key={item.id}>
              <span>
                {item.name} × {item.quantity}
              </span>

              <span>
                ${(item.price * item.quantity).toFixed(2)}
              </span>
            </div>
          ))}

          <div className="summary-total">
            <strong>Total</strong>
            <strong>${cartTotal.toFixed(2)}</strong>
          </div>
        </aside>
      </div>
    </main>
  );
}

export default CheckoutPage;