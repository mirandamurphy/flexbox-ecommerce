import { Link } from "react-router-dom";

function SubscriptionBoxCard({ box }) {
  return (
    <article className="box-card">
      {box.imageUrl && (
        <img
          className="box-card__image"
          src={box.imageUrl}
          alt={box.name}
        />
      )}

      <div className="box-card__content">
        <h2>{box.name}</h2>

        {box.description && (
          <p>{box.description}</p>
        )}

        <p className="box-card__price">
          {box.currency} ${Number(box.price).toFixed(2)}
        </p>

        <p>
          Status: {box.active ? "Available" : "Unavailable"}
        </p>

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