import { useState } from 'react';
import Modal from '../common/Modal.jsx';
import { changePassword } from '../../api/userApi.js';

export default function ChangePasswordModal({ onClose }) {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [done, setDone] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (newPassword !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }

        setSubmitting(true);
        try {
            await changePassword({ oldPassword, newPassword });
            setDone(true);
        } catch (err) {
            setError(err.response?.data?.message || 'Could not change password.');
        } finally {
            setSubmitting(false);
        }
    }

    if (done) {
        return (
            <Modal title="Password changed" onClose={onClose}>
                <div className="modal-body">
                    <p className="auth-status-text" style={{ marginBottom: 0}}>
                        Your password was updated successfully.
                    </p>
                </div>
                <div className="modal-actions">
                    <button type="button" className="btn btn-primary" onClick={onClose}>
                        Done
                    </button>
                </div>
            </Modal>
        );
    }

    return (
        <Modal title="Change password" subtitle="Enter your current password to confirm." onClose={onClose}>
            <form onSubmit={handleSubmit}>
                <div className="modal-body">
                    <div>
                        <input
                            id="old-password"
                            type="password"
                            className="text-input"
                            value={oldPassword}
                            onChange={(e) => setOldPassword(e.target.value)}
                            required
                        />
                        <label className="field-label" htmlFor="old-password">
                            Current password
                        </label>
                    </div>

                    <div>
                        <input
                            id="new-password"
                            type="password"
                            className="text-input"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                        />
                        <label className="field-label" htmlFor="new-password">
                            New password
                        </label>
                        <p className="cell-muted" style={{ marginTop: 6 }}>
                            At least 8 characters, one uppercase, one lowercase, one special character.
                        </p>
                    </div>

                    <div>
                        <input
                            id="confirm-new-password"
                            type="password"
                            className="text-input"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                        <label className="field-label" htmlFor="confirm-new-password">
                            Confirm new password
                        </label>
                    </div>

                    {error && <p className="form-error">{error}</p>}
                </div>

                <div className="modal-actions">
                    <button type="button" className="btn btn-ghost" onClick={onClose}>
                        Cancel
                    </button>
                    <button type="submit" className="btn btn-primary" disabled={submitting}>
                        {submitting ? 'Saving...' : 'Change password'}
                    </button>
                </div>
            </form>
        </Modal>
    )
}