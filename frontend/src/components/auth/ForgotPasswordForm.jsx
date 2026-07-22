import { useState } from 'react';
import { Link } from 'react-router-dom';
import * as authApi from '../../api/authApi.js';
import AuthStatus from './AuthStatus.jsx';

export default function ForgotPasswordForm() {
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [sent, setSent] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!email) {
            setError('Email is required.');
            return;
        }

        setSubmitting(true);

        try {
            await authApi.forgotPassword(email);
            setSent(true)
        } catch (err) {
            setError(err.response?.data?.message || 'Could not send the forgot password link.');
        } finally {
            setSubmitting(false);
        }
    };

    if (sent) {
        return (
            <AuthStatus
                variant="success"
                title="Check your inbox"
                message={`We've sent a password reset link to ${email}.`}
            >
                <Link to="/login" className="submit-btn">
                    Back to login
                </Link>
            </AuthStatus>
        );
    }

    return (
        <div className="auth-form-wrap">
            <h2 className="auth-form-heading">Forgot password?</h2>
            <p className="auth-form-subheading">Enter your email and we'll send you a reset link.</p>

            <form className="auth-form" onSubmit={handleSubmit} noValidate>
                <div className="field">
                    <input
                        id="forgot-email"
                        type="email"
                        placeholder=" "
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <label htmlFor="forgot-email">Email</label>
                </div>

                {error && <p className="form-error">{error}</p>}

                <button type="submit" className="submit-btn" disabled={submitting}>
                    {submitting ? 'Sending...' : 'Send reset link'}
                </button>

                <p className="form-footnote">
                    Remember it? <Link to="/login">Log in</Link>
                </p>
            </form>
        </div>
    );
}