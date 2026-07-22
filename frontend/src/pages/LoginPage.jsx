import LoginForm from "../components/auth/LoginForm.jsx";
import "../styles/auth.css"

export default function LoginPage() {
    return (
        <div className="auth-page">
            <div className="auth-card">
                <div className="auth-form-side">
                    <LoginForm />
                </div>
            </div>
        </div>
    )
}