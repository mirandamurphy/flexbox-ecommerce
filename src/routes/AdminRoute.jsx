import { Navigate, Outlet } from "react-router-dom";

import { useAuth } from "../context/AuthContext";

function AdminRoute() {
  const { currentUser, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (currentUser.role !== "ADMIN") {
    return <Navigate to="/profile" replace />;
  }

  return <Outlet />;
}

export default AdminRoute;