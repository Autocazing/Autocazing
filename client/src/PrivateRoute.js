import React from "react";
import { Navigate } from "react-router-dom";

const PrivateRoute = ({ authenticated, component: Component }) => {
    // Component 명시
    authenticated ? (
        <Component />
    ) : (
        <Navigate to="/" replace state={{ showAlert: true }} />
    );
};

export default PrivateRoute;
