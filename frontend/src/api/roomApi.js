import axiosClient from './axiosClient';

export const getAllRooms = () => axiosClient.get('/rooms');

export const getAvailableRooms = (startTime, endTime) =>
    axiosClient.get('/rooms/available', { params: { startTime, endTime } });

export const getRoomById = (id) => axiosClient.get(`/rooms/${id}`);

export const createRoom = (data) => axiosClient.post('/rooms', data);

export const deleteRoom = (id) => axiosClient.delete(`/rooms/${id}`);

export const editRoom = (id, data) => axiosClient.patch(`/rooms/${id}`, data);