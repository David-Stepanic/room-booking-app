import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import RoomBrowser from '../components/rooms/RoomBrowser.jsx';
import MyReservations from '../components/reservations/MyReservations.jsx';
import ChangePasswordModal from '../components/dashboard/ChangePasswordModal.jsx';
import '../styles/dashboard.css';

const NAV_ITEMS = [
    { id: 'browse', label: 'Browse & reserve'},
    { id: 'mine', label: 'My Reservations'},
];

export default function DashboardPage() {
    const { user, isAdmin, logoutUser } = useAuth();
    const navigate = useNavigate();
    const [tab, setTab] = useState('browse');
    const [showChangePassword, setShowChangePassword] = useState(false);

    const handleLogout = () => {
        logoutUser();
        navigate('/login');
    };

    const initials = (user?.email || '?').slice(0, 2).toUpperCase();

    return (
        <div className="dash-shell">
            <aside className="dash-sidebar">
                <div>
                    <div className="dash-brand">
                        <span className="dash-brand-name">Room Reservation App</span>
                    </div>

                    <nav className="dash-nav">
                        {NAV_ITEMS.map((item) => (
                            <button
                                key={item.id}
                                type="button"
                                className={`dash-nav-item ${tab === item.id ? 'is-active' : ''}`}
                                onClick={() => setTab(item.id)}
                            >
                                {item.label}
                            </button>
                        ))}
                    </nav>
                </div>

                <div className="dash=sidebar-footer">
                    <div className="dash-user-card">
                        <span className="dash-user-avatar">{initials}</span>
                        <div className="dash-user-meta">
                            <div className="dash-user-email">{user?.email}</div>
                            <div className="dash-user-role">{isAdmin ? 'Admin' : 'User'}</div>
                        </div>
                    </div>
                    <button
                        type="button"
                        className="dash-secondary-btn"
                        onClick={() => setShowChangePassword(true)}
                    >
                        Change password
                    </button>
                    <button type="button" className="dash-logout-btn" onClick={handleLogout}>
                        Log out
                    </button>
                </div>
            </aside>

            <main className="dash-main">
                {tab === 'browse' && <RoomBrowser />}
                {tab === 'mine' && <MyReservations />}
            </main>

            {showChangePassword && (
                <ChangePasswordModal onClose={() => setShowChangePassword(false)}/>
            )}
        </div>
    );
}