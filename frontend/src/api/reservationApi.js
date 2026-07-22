import axiosClient from './axiosClient'

export const createReservation = (data) => axiosClient.post('/reservations/make', data);

export const getAllReservations = () => axiosClient.get('/reservations');

export const confirmReservation = (id) => axiosClient.patch(`/reservations/admin/confirm/${id}`);

export const cancelReservation = (id) => axiosClient.patch(`/reservations/admin/cancel/${id}`);

export const cancelOwnReservation = (id) => axiosClient.patch(`/reservations/cancel/${id}`);

export const declineReservation = (id, data) => axiosClient.patch(`/reservations/admin/decline/${id}`, data);

export const deleteReservation = (id) => axiosClient.delete(`/reservations/admin/delete/${id}`);

