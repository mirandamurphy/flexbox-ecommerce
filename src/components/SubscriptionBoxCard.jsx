import { Link } from "react-router-dom";

function SubscriptionBoxCard({ box }) {
  return (
    <article className="box-card">
      <div className="box-card__content">
        <p className="box-card__category">{box.category}</p>

        <h2>{box.name}</h2>

        <p>{box.description}</p>

        <p className="box-card__price">
          ${box.price.toFixed(2)} / month
        </p>

        <p>Stock: {box.stock}</p>

        <Link
          className="primary-button"
          to={`/boxes/${box.id}`}
        >
          View Details
        </Link>
      </div>
    </article>
  );
}

export default SubscriptionBoxCard;