import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as authApi from '../../api/authApi.js';
import AuthStatus from './AuthStatus.jsx';

export default function ResetPasswordForm({ token }) {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('')
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [done, setDone] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!newPassword, !confirmPassword) {
            setError('All fields are required!');
            return;
        }

        if (newPassword !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }

        setSubmitting(true);

        try {
            await authApi.resetPassword({ token, newPassword });
            setDone(true);
        } catch (err) {
            setError(err.response?.data?.message || 'Could not reset your password.')
        } finally {
            setSubmitting(false);
        }
    };

    if (done) {
        return (
            <AuthStatus
                variant="success"
                title="Password updated"
                message="You can now log in with your new password."
            >
                <button type="submit" className="submit-btn" onClick={() => navigate('/login')}>
                    Go to login
                </button>
            </AuthStatus>
        );
    }

    return (
        <div className="auth-form-wrap">
            <h2 className="auth-form-heading">Set a new password</h2>
            <p className="auth-form-subheading">Choose something you haven't used before.</p>

            <form className="auth-form" onSubmit={handleSubmit} noValidate>
                <div className="field">
                    <input
                        id="new-password"
                        name="new-password"
                        placeholder=" "
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        required
                    />
                    <label htmlFor="new-password">New password</label>
                </div>

                <div className="field">
                    <input
                        id="confirm-password"
                        name="confirm-password"
                        placeholder=" "
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                    <label htmlFor="confirm-password">Confirm password</label>
                </div>

                {error && <p className="form-error">{error}</p>}

                <button type="submit" className="submit-btn" disabled={submitting}>
                    {submitting ? 'Saving...' : 'Reset password'}
                </button>
            </form>
        </div>
    );
}