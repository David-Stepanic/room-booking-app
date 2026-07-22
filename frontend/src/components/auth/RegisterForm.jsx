import { useState } from "react";
import { useAuth } from "../../context/AuthContext.jsx";
import { Link } from "react-router-dom";
import AuthStatus from "./AuthStatus";
import * as authApi from "../../api/authApi.js";

export default function RegisterForm() {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
    });
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const { registerUser } = useAuth();

    const [registeredEmail, setRegisteredEmail] = useState('');
    const [resendState, setResendState] = useState('idle');
    const [resendError, setResendError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!formData.firstName || !formData.lastName || !formData.email || !formData.password) {
            setError('All fields are required!');
            return;
        }

        setSubmitting(true);

        try {
            const response = await registerUser(formData);
            setRegisteredEmail(response.email);
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed.');
        } finally {
            setSubmitting(false);
        }
    };

    const handleResend = async () => {
        setResendState('sending');
        setResendError('');
        try {
            await authApi.resendVerification(registeredEmail);
            setResendState('sent');
        } catch (err) {
            setResendError(err.response?.data?.message || 'Could not resend the email.');
            setResendState('idle');
        }
    };

    if (registeredEmail) {
        return (
            <AuthStatus
                variant="success"
                title="Check your email"
                message={`We've sent a verification link to ${registeredEmail}. Click it to activate your account before
                logging in.`}
            >
                {resendState === 'sent' ? (
                    <p className="auth-status-text" style={{marginBottom: 0 }}>
                        New link sent - check your inbox.
                    </p>
                ) : (
                    <button
                        type="button"
                        className="link-btn"
                        onClick={handleResend}
                        disabled={resendState === 'sending'}
                    >
                        {resendState === 'sending' ? 'Sending...' : "Didn't get it? Resend email"}
                    </button>
                )}

                {resendError && <p className="form-error">{resendError}</p>}

                <Link to="/login" className="submit-btn">
                    Back to login
                </Link>
            </AuthStatus>
        );
    }

    return (
        <div className="auth-form-wrap">
            <h2 className="auth-form-heading">Create account</h2>
            <p className="auth-form-subheading">A few details and you're in.</p>

            <form className="auth-form" onSubmit={handleSubmit} noValidate>
                <div className="name-row">
                    <div className="field">
                        <input
                            id="firstName"
                            name="firstName"
                            placeholder=" "
                            value={formData.firstName}
                            onChange={handleChange}
                            required
                        />
                        <label htmlFor="firstName">First name</label>
                    </div>
                    <div className="field">
                        <input
                            id="lastName"
                            name="lastName"
                            placeholder=" "
                            value={formData.lastName}
                            onChange={handleChange}
                            required
                        />
                        <label htmlFor="lastName">Last name</label>
                    </div>
                </div>
                <div className="field">
                    <input
                        id="email"
                        name="email"
                        type="email"
                        placeholder=" "
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                    <label htmlFor="email">Email</label>
                </div>
                <div className="field">
                    <input
                        id="password"
                        name="password"
                        type="password"
                        placeholder=" "
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                    <label htmlFor="password">Password</label>
                </div>

                {error && <p className="form-error">{error}</p>}

                <button type="submit" className="submit-btn" disabled={submitting}>
                    {submitting ? 'Creating account...' : 'Create account'}
                </button>

                <p className="form-footnote">
                    Already have an account? <Link to="/login">Log in</Link>
                </p>
            </form>
        </div>
    );
}
