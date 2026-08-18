import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

import { useCart } from "../context/CartContext";
import { getBoxById } from "../services/boxService";

function BoxDetailsPage() {
  const { id } = useParams();
  const { addToCart } = useCart();

  const [box, setBox] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadBox() {
      try {
        setIsLoading(true);
        setError("");

        const response = await getBoxById(id);
        setBox(response.data);
      } catch (apiError) {
        const message =
          apiError.response?.data?.detail ||
          apiError.message ||
          "Unable to load subscription box.";

        setError(message);
      } finally {
        setIsLoading(false);
      }
    }

    loadBox();
  }, [id]);

  if (isLoading) {
    return (
      <main className="page">
        <p>Loading subscription box...</p>
      </main>
    );
  }

  if (error || !box) {
    return (
      <main className="page">
        <h1>Subscription Box Not Found</h1>

        {error && (
          <p className="error-message">{error}</p>
        )}

        <Link to="/boxes">
          Back to Boxes
        </Link>
      </main>
    );
  }

  return (
    <main className="page">
      <section className="box-details">
        <h1>{box.name}</h1>

        <p>{box.description}</p>

        <p className="box-card__price">
          {box.currency} ${Number(box.price).toFixed(2)}
        </p>

        <p>
          Status: {box.active ? "Available" : "Unavailable"}
        </p>

        {box.imageUrl && (
          <img
            src={box.imageUrl}
            alt={box.name}
          />
        )}

        <button
          className="primary-button"
          type="button"
          onClick={() => addToCart(box)}
          disabled={!box.active}
        >
          Add to Cart
        </button>
      </section>
    </main>
  );
}

export default BoxDetailsPage;