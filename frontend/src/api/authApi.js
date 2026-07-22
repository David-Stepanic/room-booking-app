import axiosClient from './axiosClient'

export const register = (data) => {
    return axiosClient.post('/auth/register', data);
};

export const login = (data) => {
    return axiosClient.post('/auth/login', data);
};

export const verifyEmail = (token) => {
    return axiosClient.get('/auth/verify', { params: { token } });
};

export const resendVerification = (email) => {
    return axiosClient.post('/auth/resend-verification', { email });
};

export const forgotPassword = (email) => {
    return axiosClient.post('/auth/forgot-password', { email });
};

export const resetPassword = (data) => {
    return axiosClient.post('/auth/reset-password', data);
};



