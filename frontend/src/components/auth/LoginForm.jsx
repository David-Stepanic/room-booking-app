import { useState } from "react";
import { useNavigate, Link} from "react-router-dom";
import { useAuth } from "../../context/AuthContext.jsx";
import '../../styles/auth.css';

export default function LoginForm() {

    const [formData, setFormData] = useState({ email: '', password: ''});
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const { loginUser } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value})
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!formData.email || !formData.password) {
            setError('All fields are required!');
            return;
        }

        setSubmitting(true);
        try {
            await loginUser(formData);
            navigate('/dashboard');
        } catch (err) {
            setError(err.response?.data?.message || 'Login failed. Check your credentials.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="auth-form-wrap">
            <h2 className="auth-form-heading">Log in</h2>
            <p className="auth-form-subheading">Enter your details to continue.</p>

            <form className="auth-form" onSubmit={handleSubmit} noValidate>
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
                    {submitting ? 'Logging on' : 'Log in'}
                </button>

                <p className="form-footnote">
                    Don't have an account? <Link to="/register">Create new account</Link>
                </p>
                <p className="form-footnote">
                    <Link to="/forgot-password">Forgot your password?</Link>
                </p>
            </form>
        </div>
    );
}