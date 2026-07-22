import { FiLoader, FiCheckCircle, FiXCircle } from 'react-icons/fi';

export default function AuthStatus({ variant = 'loading', title, message, children }) {
    return (
        <div className="auth-status">
            <div className={`auth-status-icon auth-status-icon--${variant}`}>
                {variant === 'loading' &&  <FiLoader className="spinner"/>}
                {variant === 'success' && <FiCheckCircle />}
                {variant === 'error' && <FiXCircle />}
            </div>
            <h2 className="auth-status-title">{title}</h2>
            {message && <p className="auth-status-text">{message}</p>}
            {children && <p className="auth-status-actions">{children}</p>}
        </div>
    );
}