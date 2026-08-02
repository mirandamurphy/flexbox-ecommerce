import { Link } from "react-router-dom";

function HomePage() {
  return (
    <main className="home-page">
      <section className="hero">
        <div>
          <p className="eyebrow">Fitness subscription boxes</p>

          <h1>Build your fitness routine with Flexbox.</h1>

          <p>
            Discover curated fitness, wellness, and recovery products delivered
            in convenient subscription boxes.
          </p>

          <div className="hero-actions">
            <Link className="primary-button" to="/boxes">
              Browse Boxes
            </Link>

            <Link className="secondary-button" to="/register">
              Create Account
            </Link>
          </div>
        </div>
      </section>

      <section className="feature-section">
        <h2>Why choose Flexbox?</h2>

        <div className="feature-grid">
          <article className="feature-card">
            <h3>Curated Products</h3>
            <p>Fitness products selected for different training goals.</p>
          </article>

          <article className="feature-card">
            <h3>Flexible Plans</h3>
            <p>Choose subscription options that match your lifestyle.</p>
          </article>

          <article className="feature-card">
            <h3>Secure Checkout</h3>
            <p>Complete payments through a safe checkout experience.</p>
          </article>
        </div>
      </section>
    </main>
  );
}

export default HomePage;