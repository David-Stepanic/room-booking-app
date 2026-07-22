import { useEffect, useState, useRef } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import * as authApi from '../api/authApi.js';
import AuthStatus from '../components/auth/AuthStatus.jsx';
import '../styles/auth.css';

export default function VerifyEmailPage() {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');

    // status options: loading | success | already | sent
    const [status, setStatus] = useState('loading');
    const [message, setMessage] = useState('');

    const [resendEmail, setResendEmail] = useState('');
    // resendState options: idle | sending | sent
    const [resendState, setResendState] = useState('idle');
    const [resendError, setResendError] = useState('');

    const hasRun = useRef(false);

    useEffect(() => {
        if (!token) {
            setStatus('error');
            setMessage("This verification link is missing it's token");
            return;
        }

        if (hasRun.current) return;
        hasRun.current = true;

        authApi
            .verifyEmail(token)
            .then(() => setStatus('success'))
            .catch((err) => {
                const code = err.response?.data?.code;
                const msg = err.response?.data?.message || 'This link is invalid or has expired.';

                setMessage(msg);
                setStatus(code === 'ALREADY_VERIFIED' ? 'already' : 'error');
            });
    }, [token]);

    const handleResend = async (e) => {
        e.preventDefault();
        setResendState('sending');
        setResendError('');
        try {
            await authApi.resendVerification(resendEmail);
            setResendState('sent');
        } catch (err) {
            setResendError(err.response?.data?.message || 'Could not resend the email.');
            setResendState('idle');
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <div className="auth-form-side">
                    {status === 'loading' && (
                        <AuthStatus variant="loading" title="Verifying your email" message="Just a moment..." />
                    )}

                    {status === "success" && (
                        <AuthStatus
                            variant="success"
                            title="Email verified"
                            message="Your account is active. You can log in now."
                        >
                            <Link to="/login" className="submit-btn">
                                Go to login
                            </Link>
                        </AuthStatus>
                    )}

                    {status === 'already' && (
                        <AuthStatus
                            variant="success"
                            title="Already verified"
                            message="This account has already been verified — you're good to go."
                        >
                            <Link to="/login" className="submit-btn">
                                Go to login
                            </Link>
                        </AuthStatus>
                    )}

                    {status === "error" && (
                        <AuthStatus variant="error" title="Verification failed" message={message}>
                            {resendState === 'send' ? (
                                <p className="auth-status-text" style={{ marginBottom: 0 }}>
                                    New link sent - check your inbox.
                                </p>
                            ) : (
                                <form onSubmit={handleResend} className="auth-form">
                                    <div className="field">
                                        <input
                                            type="email"
                                            placeholder=" "
                                            value={resendEmail}
                                            onChange={(e) => setResendEmail(e.target.value)}
                                            required
                                        />
                                        <label>Email</label>
                                    </div>
                                    {resendError && <p className="form-error">{resendError}</p>}
                                    <button type="submit" className="submit-btn" disabled={resendState === 'sending'}>
                                        {resendState === 'sending' ? 'Sending...' : 'Resend verification email'}
                                    </button>
                                </form>
                            )}
                            <p className="form-footnote">
                                <Link to="/login">Back to login</Link>
                            </p>
                        </AuthStatus>
                    )}
                </div>
            </div>
        </div>
    );
}
