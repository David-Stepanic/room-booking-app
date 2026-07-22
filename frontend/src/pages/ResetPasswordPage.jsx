import { useSearchParams, Link } from 'react-router-dom';
import ResetPasswordForm from '../components/auth/ResetPasswordForm.jsx';
import AuthStatus from '../components/auth/AuthStatus.jsx';
import '../styles/auth.css';

export default function ResetPasswordPage() {
    const [searchParams] = useSearchParams(); // TODO: objasnjenje searchParams i ostale forme i stranice
    const token = searchParams.get('token');

    return (
        <div className="auth-page">
            <div className="auth-card">
                <div className="auth-form-side">
                    {token ? (
                        <ResetPasswordForm token={token}/>
                    ) : (
                        <AuthStatus
                            variant="error"
                            title="Invalid link"
                            message="This password reset link is missing its token."
                        >
                            <Link to="/forgot-password" className="submit-btn">
                                Request a new link
                            </Link>
                        </AuthStatus>
                    )}
                </div>
            </div>
        </div>
    );
}