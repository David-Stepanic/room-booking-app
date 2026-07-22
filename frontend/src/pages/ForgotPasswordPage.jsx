import ForgotPasswordForm from '../components/auth/ForgotPasswordForm.jsx';
import '../styles/auth.css';

export default function ForgotPasswordPage() {
    return (
        <div className="auth-page">
            <div className="auth-card">
                <div className="auth-form-side">
                    <ForgotPasswordForm />
                </div>
            </div>
        </div>
    )
}