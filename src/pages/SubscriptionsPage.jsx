function SubscriptionsPage() {
  const subscriptions = [
    {
      id: 1,
      boxName: "Performance Box",
      plan: "Monthly",
      price: 39.99,
      status: "Active",
      nextBillingDate: "2026-08-15",
    },
  ];

  return (
    <main className="page">
      <section className="page-header">
        <h1>Active Subscriptions</h1>
        <p>Manage your current fitness subscription plans.</p>
      </section>

      <section className="subscription-list">
        {subscriptions.map((subscription) => (
          <article
            className="subscription-card"
            key={subscription.id}
          >
            <div>
              <p className="box-card__category">
                {subscription.plan} Plan
              </p>

              <h2>{subscription.boxName}</h2>

              <p>
                ${subscription.price.toFixed(2)} per month
              </p>

              <p>
                Next billing date:{" "}
                {subscription.nextBillingDate}
              </p>
            </div>

            <span className="status-badge">
              {subscription.status}
            </span>
          </article>
        ))}
      </section>
    </main>
  );
}

export default SubscriptionsPage;