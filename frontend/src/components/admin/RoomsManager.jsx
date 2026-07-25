import { useEffect, useState } from 'react';
import { getAllRooms, deleteRoom } from '../../api/roomApi';
import { roomTypeLabel } from '../../utils/enums';
import RoomFormModal from './RoomFormModal';

export default function RoomsManager() {
    const [rooms, setRooms] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [busyId, setBusyId] = useState(null);
    const [formTarget, setFormTarget] = useState(null); // null = closed, {} = create, room = edit
    const [showForm, setShowForm] = useState(false);

    const load = () => {
        setLoading(true);
        getAllRooms()
            .then((res) => setRooms(res.data))
            .catch((err) => setError(err.response?.data?.message || 'Could not load rooms.'))
            .finally(() => setLoading(false));
    };

    useEffect(load, []);

    const handleDelete = async (id) => {
        setBusyId(id);
        setError('');
        try {
            await deleteRoom(id);
            load();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not delete room.');
        } finally {
            setBusyId(null);
        }
    };

    const openCreate = () => {
        setFormTarget(null);
        setShowForm(true);
    };

    const openEdit = (room) => {
        setFormTarget(room);
        setShowForm(true);
    };

    const closeForm = () => setShowForm(false);

    const handleSaved = () => {
        setShowForm(false);
        load();
    };

    return (
        <div>
            <div className="dash-header" style={{ display: 'flex', alignItems: 'flex-end', justifyContent: 'space-between', marginBottom: '24px'}}>
                <div>
                    <h1 className="dash-title">Rooms</h1>
                    <p className="dash-subtitle">Add, edit, or remove bookable rooms.</p>
                </div>
                <button type="button" className="btn btn-primary" onClick={openCreate}>
                    Add room
                </button>
            </div>

            {error && <div className="banner banner--error">{error}</div>}
            {loading && <div className="loading-state">Loading rooms…</div>}

            {!loading && rooms && rooms.length === 0 && (
                <div className="empty-state">
                    <p className="empty-state-title">No rooms yet</p>
                    <p>Add your first bookable room to get started.</p>
                </div>
            )}

            {!loading && rooms && rooms.length > 0 && (
                <div className="table-wrap">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Room number</th>
                            <th>Type</th>
                            <th>Capacity</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {rooms.map((room) => (
                            <tr key={room.id}>
                                <td>Room {room.roomNumber}</td>
                                <td>{roomTypeLabel(room.roomType)}</td>
                                <td>{room.capacity}</td>
                                <td>
                                    <div className="table-actions">
                                        <button
                                            type="button"
                                            className="btn btn-ghost btn-sm"
                                            onClick={() => openEdit(room)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            type="button"
                                            className="btn btn-danger btn-sm"
                                            disabled={busyId === room.id}
                                            onClick={() => handleDelete(room.id)}
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}

            {showForm && (
                <RoomFormModal room={formTarget} onClose={closeForm} onSaved={handleSaved} />
            )}
        </div>
    );
}