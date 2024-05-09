import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useEffect } from "react";

const PrivateRoute = () => {
    const access = localStorage.getItem("accessToken");
    // Component 명시

    useEffect(() => {
        if (!access) {
            alert("권한이 없습니다. 로그인 해주세요.");
            window.location.reload();
        }
    }, [access]);

    return access ? <Outlet /> : <Navigate to="/" replace />;
};

export default PrivateRoute;
