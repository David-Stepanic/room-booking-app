import { useState } from 'react';
import Modal from '../common/Modal';
import { createRoom, editRoom } from '../../api/roomApi';
import { ROOM_TYPE_OPTIONS } from '../../utils/enums';

export default function RoomFormModal({ room, onClose, onSaved }) {
    const isEdit = Boolean(room);
    const [roomNumber, setRoomNumber] = useState(room?.roomNumber ?? '');
    const [capacity, setCapacity] = useState(room?.capacity ?? '');
    const [roomType, setRoomType] = useState(room?.roomType ?? ROOM_TYPE_OPTIONS[0].value);
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSubmitting(true);
        try {
            const payload = {
                roomNumber: Number(roomNumber),
                capacity: Number(capacity),
                roomType,
            };
            if (isEdit) {
                await editRoom(room.id, payload);
            } else {
                await createRoom(payload);
            }
            onSaved();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not save the room.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Modal title={isEdit ? 'Edit room' : 'Add room'} onClose={onClose}>
            <form onSubmit={handleSubmit}>
                <div className="modal-body">
                    <div>
                        <label className="field-label" htmlFor="room-number">
                            Room number
                        </label>
                        <input
                            id="room-number"
                            type="number"
                            min="1"
                            className="text-input"
                            value={roomNumber}
                            onChange={(e) => setRoomNumber(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label className="field-label" htmlFor="room-capacity">
                            Capacity
                        </label>
                        <input
                            id="room-capacity"
                            type="number"
                            min="1"
                            className="text-input"
                            value={capacity}
                            onChange={(e) => setCapacity(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label className="field-label" htmlFor="room-type">
                            Room type
                        </label>
                        <select
                            id="room-type"
                            className="select-input"
                            value={roomType}
                            onChange={(e) => setRoomType(e.target.value)}
                        >
                            {ROOM_TYPE_OPTIONS.map((opt) => (
                                <option key={opt.value} value={opt.value}>
                                    {opt.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    {error && <p className="form-error">{error}</p>}
                </div>

                <div className="modal-actions">
                    <button type="button" className="btn btn-ghost" onClick={onClose}>
                        Cancel
                    </button>
                    <button type="submit" className="btn btn-primary" disabled={submitting}>
                        {submitting ? 'Saving…' : isEdit ? 'Save changes' : 'Add room'}
                    </button>
                </div>
            </form>
        </Modal>
    );
}