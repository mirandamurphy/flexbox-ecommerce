import { Link, useNavigate } from "react-router-dom";

import { useAuth } from "../context/AuthContext";

function ProfilePage() {
  const navigate = useNavigate();
  const { currentUser, logout } = useAuth();

  if (!currentUser) {
    return (
      <main className="page">
        <h1>Profile</h1>
        <p>You must log in to view your profile.</p>
        <Link className="primary-button" to="/login">
          Go to Login
        </Link>
      </main>
    );
  }

  function handleLogout() {
    logout();
    navigate("/");
  }

  return (
    <main className="page">
      <section className="profile-card">
        <h1>My Profile</h1>

        <dl className="profile-details">
          <div>
            <dt>Name</dt>
            <dd>{currentUser.name}</dd>
          </div>

          <div>
            <dt>Email</dt>
            <dd>{currentUser.email}</dd>
          </div>

          <div>
            <dt>Role</dt>
            <dd>{currentUser.role}</dd>
          </div>
        </dl>

        <div className="profile-actions">
          <Link className="secondary-button" to="/orders">
            Order History
          </Link>

          <Link className="secondary-button" to="/subscriptions">
            Active Subscriptions
          </Link>

          <button type="button" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </section>
    </main>
  );
}

export default ProfilePage;