import { Link } from "react-router-dom";

function OrderConfirmationPage() {
  const savedOrder = localStorage.getItem(
    "latestFlexboxOrder"
  );

  const order = savedOrder ? JSON.parse(savedOrder) : null;

  if (!order) {
    return (
      <main className="page">
        <h1>No Recent Order</h1>
        <Link to="/boxes">Browse Subscription Boxes</Link>
      </main>
    );
  }

  return (
    <main className="page">
      <section className="confirmation-card">
        <p className="success-badge">Payment Successful</p>

        <h1>Thank you, {order.customerName}!</h1>

        <p>
          Your order has been created successfully. A confirmation
          email will be sent when the email service is connected.
        </p>

        <dl className="order-details-list">
          <div>
            <dt>Order Number</dt>
            <dd>{order.orderNumber}</dd>
          </div>

          <div>
            <dt>Order Date</dt>
            <dd>{order.date}</dd>
          </div>

          <div>
            <dt>Total</dt>
            <dd>${order.total.toFixed(2)}</dd>
          </div>
        </dl>

        <div className="confirmation-actions">
          <Link className="primary-button" to="/orders">
            View Order History
          </Link>

          <Link className="secondary-button" to="/boxes">
            Continue Shopping
          </Link>
        </div>
      </section>
    </main>
  );
}

export default OrderConfirmationPage;