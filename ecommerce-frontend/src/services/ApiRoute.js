import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import ApiService from "./ApiService";

export const protectRoute = ({ element: Component }) => {
    const location = useLocation();
    const isAuthenticated = ApiService.isAuthenticated();
    if (isAuthenticated) {
        return Component;
    } else {
        return <Navigate to={{ pathname: '/login', state: { from: location } }} />
    };
};

export const protectAdminRoute = ({ element: Component }) => {
    const location = useLocation();
    const isAdminAuthenticated = ApiService.isAdmin();
    if (isAdminAuthenticated) {
        return Component;
    } else {
        return <Navigate to={{ pathname: '/login', state: { from: location } }} />
    };
};

