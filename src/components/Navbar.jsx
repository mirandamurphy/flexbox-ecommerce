import { NavLink, useNavigate } from "react-router-dom";

import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";

function Navbar() {
  const navigate = useNavigate();
  const { cartCount } = useCart();
  const { currentUser, logout } = useAuth();

  function handleLogout() {
    logout();
    navigate("/");
  }

  return (
    <header className="navbar">
      <NavLink className="brand" to="/">
        Flexbox
      </NavLink>

      <nav className="nav-links">
        <NavLink to="/">Home</NavLink>
        <NavLink to="/boxes">Boxes</NavLink>
        <NavLink to="/cart">Cart ({cartCount})</NavLink>

        {currentUser ? (
          <>
            <NavLink to="/profile">Profile</NavLink>

            {currentUser.role === "ADMIN" && (
              <NavLink to="/admin">Admin</NavLink>
            )}

            <button
              className="nav-button"
              type="button"
              onClick={handleLogout}
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <NavLink to="/login">Login</NavLink>
            <NavLink to="/register">Register</NavLink>
          </>
        )}
      </nav>
    </header>
  );
}

export default Navbar;