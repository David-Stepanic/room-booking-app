import { useState } from 'react';
import Modal from '../common/Modal';

export default function DeclineModal({ onClose, onSubmit }) {
    const [reason, setReason] = useState('');
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!reason.trim()) {
            setError('A reason is required.');
            return;
        }
        setSubmitting(true);
        try {
            await onSubmit(reason.trim());
        } catch (err) {
            setError(err.response?.data?.message || 'Could not decline the reservation.');
            setSubmitting(false);
        }
    };

    return (
        <Modal title="Decline reservation" subtitle="Let the requester know why." onClose={onClose}>
            <form onSubmit={handleSubmit}>
                <div className="modal-body">
                    <div>
                        <label className="field-label" htmlFor="decline-reason">
                            Reason
                        </label>
                        <textarea
                            id="decline-reason"
                            className="textarea-input"
                            value={reason}
                            onChange={(e) => setReason(e.target.value)}
                            placeholder="e.g. Room is booked for maintenance that day"
                        />
                    </div>
                    {error && <p className="form-error">{error}</p>}
                </div>
                <div className="modal-actions">
                    <button type="button" className="btn btn-ghost" onClick={onClose}>
                        Cancel
                    </button>
                    <button type="submit" className="btn btn-danger" disabled={submitting}>
                        {submitting ? 'Declining…' : 'Decline'}
                    </button>
                </div>
            </form>
        </Modal>
    );
}