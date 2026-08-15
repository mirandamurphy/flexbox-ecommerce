import { useState } from "react";

import SubscriptionBoxCard from "../components/SubscriptionBoxCard";
import mockBoxes from "../data/mockBoxes";

function BoxesPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [category, setCategory] = useState("All");

  const filteredBoxes = mockBoxes.filter((box) => {
    const matchesSearch = box.name
      .toLowerCase()
      .includes(searchTerm.toLowerCase());

    const matchesCategory =
      category === "All" || box.category === category;

    return matchesSearch && matchesCategory;
  });

  return (
    <main className="page">
      <section className="page-header">
        <h1>Subscription Boxes</h1>
        <p>
          Browse curated fitness boxes based on your goals.
        </p>
      </section>

      <section className="catalog-controls">
        <input
          type="search"
          placeholder="Search subscription boxes..."
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.target.value)}
        />

        <select
          value={category}
          onChange={(event) => setCategory(event.target.value)}
        >
          <option value="All">All Categories</option>
          <option value="Performance">Performance</option>
          <option value="Wellness">Wellness</option>
          <option value="Beginner">Beginner</option>
        </select>
      </section>

      <section className="box-grid">
        {filteredBoxes.length > 0 ? (
          filteredBoxes.map((box) => (
            <SubscriptionBoxCard
              key={box.id}
              box={box}
            />
          ))
        ) : (
          <p>No subscription boxes found.</p>
        )}
      </section>
    </main>
  );
}

export default BoxesPage;