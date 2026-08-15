import { useAuth } from "../context/AuthContext";

function AdminDashboardPage() {
  const { currentUser } = useAuth();

  if (!currentUser || currentUser.role !== "ADMIN") {
    return (
      <main className="page">
        <h1>Unauthorized</h1>
        <p>You must log in as an administrator.</p>
      </main>
    );
  }

  const products = [
    {
      id: 1,
      name: "Performance Box",
      category: "Performance",
      price: 39.99,
      stock: 12,
    },
    {
      id: 2,
      name: "Wellness Box",
      category: "Wellness",
      price: 34.99,
      stock: 8,
    },
    {
      id: 3,
      name: "Beginner Box",
      category: "Beginner",
      price: 29.99,
      stock: 20,
    },
  ];

  return (
    <main className="page">
      <section className="page-header">
        <h1>Admin Dashboard</h1>
        <p>
          Manage Flexbox products, inventory, and customer orders.
        </p>
      </section>

      <section className="admin-stats">
        <article>
          <h2>3</h2>
          <p>Subscription Boxes</p>
        </article>

        <article>
          <h2>40</h2>
          <p>Total Inventory</p>
        </article>

        <article>
          <h2>12</h2>
          <p>Customer Orders</p>
        </article>

        <article>
          <h2>8</h2>
          <p>Active Subscriptions</p>
        </article>
      </section>

      <section className="admin-section">
        <div className="admin-section__header">
          <h2>Subscription Box Inventory</h2>

          <button className="primary-button" type="button">
            Add New Box
          </button>
        </div>

        <div className="table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Category</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {products.map((product) => (
                <tr key={product.id}>
                  <td>{product.name}</td>
                  <td>{product.category}</td>
                  <td>${product.price.toFixed(2)}</td>
                  <td>{product.stock}</td>
                  <td>
                    <button type="button">Edit</button>
                    <button type="button">Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </main>
  );
}

export default AdminDashboardPage;