import { Link, useParams } from "react-router-dom";

import mockBoxes from "../data/mockBoxes";
import { useCart } from "../context/CartContext";

function BoxDetailsPage() {
  const { id } = useParams();
  const { addToCart } = useCart();

  const box = mockBoxes.find(
    (item) => item.id === Number(id)
  );

  if (!box) {
    return (
      <main className="page">
        <h1>Subscription Box Not Found</h1>
        <Link to="/boxes">Back to Boxes</Link>
      </main>
    );
  }

  return (
    <main className="page">
      <section className="box-details">
        <p className="box-card__category">
          {box.category}
        </p>

        <h1>{box.name}</h1>

        <p>{box.description}</p>

        <p className="box-card__price">
          ${box.price.toFixed(2)} / month
        </p>

        <p>Stock available: {box.stock}</p>

        <button
          className="primary-button"
          type="button"
          onClick={() => addToCart(box)}
          disabled={box.stock <= 0}
        >
          Add to Cart
        </button>
      </section>
    </main>
  );
}

export default BoxDetailsPage;