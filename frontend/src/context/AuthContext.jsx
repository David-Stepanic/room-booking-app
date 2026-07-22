import { createContext, useContext, useState, useEffect } from 'react';
import * as authApi from '../api/authApi';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const savedUser = localStorage.getItem('user');
        if(token && savedUser) {
            setUser(JSON.parse(savedUser));
        }
        setLoading(false);
    },[]);

    const loginUser = async (credentials) => {
        const response = await authApi.login(credentials);
        const {token, ...userData} = response.data;

        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(userData));
        setUser(userData);

        return userData;
    };

    const logoutUser = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setUser(null);
    }

    const registerUser = async (data) => {
        const response = await authApi.register(data);
        return response.data;
    }

    const forgotPassword = async (data) => {
        const response = await authApi.forgotPassword(data);
        return response.data;
    }

    const resetPassword = async (data) => {
        const response = await authApi.resetPassword(data);
        return response.data;
    }

    const value = {
        user,
        isAuthenticated: !!user,
        isAdmin: user?.role === 'ADMIN',
        loading,
        loginUser,
        logoutUser,
        registerUser,
        forgotPassword,
        resetPassword,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}









