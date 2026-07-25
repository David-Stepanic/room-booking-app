import { useState } from 'react';
import Modal from '../common/Modal';
import { createReservation } from '../../api/reservationApi';
import { roomTypeLabel } from '../../utils/enums';
import { formatDateTime, toApiDateTime } from '../../utils/datetime';
import { FiArrowRight } from 'react-icons/fi';

export default function ReservationModal({ room, startTime, endTime, onClose, onCreated }) {
    const [title, setTitle] = useState('');
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSubmitting(true);

        try {
            await createReservation({
                roomId: room.id,
                title,
                startTime: toApiDateTime(startTime),
                endTime: toApiDateTime(endTime),
            });
            onCreated();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not create the reservation.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Modal
            title="Reserve this room"
            subtitle={`Room ${room.roomNumber} . ${roomTypeLabel(room.roomType)}`}
            onClose={onClose}
        >
            <form onSubmit={handleSubmit}>
                 <div className="modal-body">
                     <div className="readonly-summary">
                         <strong>{formatDateTime(startTime)}</strong>
                            <FiArrowRight />
                         <strong>{formatDateTime(endTime)}</strong>
                     </div>
                     <div>
                        <label className="field-label" htmlFor="reservation-title">
                            What's this booking for?
                        </label>
                         <input
                            id="reservation-title"
                            className="text-input"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            placeholder="e.g. Sprint planning"
                            required
                         />
                     </div>

                     {error && <p className="form-error">{error}</p>}
                 </div>

                <div className="modal-actions">
                    <button type="button" className="btn btn-ghost" onClick={onClose}>
                        Cancel
                    </button>
                    <button type="submit" className="btn btn-primary" disabled={submitting}>
                        {submitting ? 'Reserving...' : 'Confirm reservation'}
                    </button>
                </div>
            </form>
        </Modal>
    );
}