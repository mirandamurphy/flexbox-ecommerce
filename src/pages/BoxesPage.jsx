import { useEffect, useState } from "react";

import SubscriptionBoxCard from "../components/SubscriptionBoxCard";
import { getBoxes } from "../services/boxService";

function BoxesPage() {
  const [boxes, setBoxes] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadBoxes() {
      try {
        setIsLoading(true);
        setError("");

        const response = await getBoxes();

        setBoxes(response.data.data || []);
      } catch (apiError) {
        const message =
          apiError.response?.data?.detail ||
          apiError.message ||
          "Unable to load subscription boxes.";

        setError(message);
      } finally {
        setIsLoading(false);
      }
    }

    loadBoxes();
  }, []);

  const filteredBoxes = boxes.filter((box) =>
    box.name
      .toLowerCase()
      .includes(searchTerm.toLowerCase())
  );

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
          onChange={(event) =>
            setSearchTerm(event.target.value)
          }
        />
      </section>

      {isLoading && (
        <p>Loading subscription boxes...</p>
      )}

      {error && (
        <p className="error-message">{error}</p>
      )}

      {!isLoading && !error && (
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
      )}
    </main>
  );
}

export default BoxesPage;