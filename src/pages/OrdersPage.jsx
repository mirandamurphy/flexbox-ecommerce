import { Link } from "react-router-dom";

function OrdersPage() {
  const savedOrder = localStorage.getItem("latestFlexboxOrder");
  const latestOrder = savedOrder ? JSON.parse(savedOrder) : null;

  const mockOrders = latestOrder
    ? [latestOrder]
    : [
        {
          orderNumber: "FB-10001",
          date: "2026-07-15",
          total: 39.99,
          status: "Completed",
          items: [
            {
              id: 1,
              name: "Performance Box",
              quantity: 1,
              price: 39.99,
            },
          ],
        },
      ];

  return (
    <main className="page">
      <section className="page-header">
        <h1>Order History</h1>
        <p>Review your previous Flexbox orders.</p>
      </section>

      <section className="order-list">
        {mockOrders.map((order) => (
          <article className="order-card" key={order.orderNumber}>
            <div className="order-card__header">
              <div>
                <h2>{order.orderNumber}</h2>
                <p>Ordered on {order.date}</p>
              </div>

              <span className="status-badge">
                {order.status || "Processing"}
              </span>
            </div>

            <div className="order-card__items">
              {order.items?.map((item) => (
                <div key={item.id}>
                  <span>
                    {item.name} × {item.quantity}
                  </span>

                  <span>
                    ${(item.price * item.quantity).toFixed(2)}
                  </span>
                </div>
              ))}
            </div>

            <div className="order-card__total">
              <strong>Total</strong>
              <strong>${Number(order.total).toFixed(2)}</strong>
            </div>
          </article>
        ))}
      </section>

      <Link className="secondary-button" to="/boxes">
        Continue Shopping
      </Link>
    </main>
  );
}

export default OrdersPage;