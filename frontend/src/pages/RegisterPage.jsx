import RegisterForm from "../components/auth/RegisterForm.jsx";
import "../styles/auth.css";

export default function RegisterPage() {
    return (
        <div className="auth-page">
            <div className="auth-card">
                <div className="auth-form-side">
                    <RegisterForm />
                </div>
            </div>
        </div>
    )
}