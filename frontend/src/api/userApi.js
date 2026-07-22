import axiosClient from './axiosClient.js';

export const getUsers = () => axiosClient.get('/users');

export const deleteUser = (id) => axiosClient.delete(`/users/${id}`);

export const changePassword = (data) => axiosClient.patch('/users/change-password', data);
