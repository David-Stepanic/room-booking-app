import { useState } from 'react';
import ReservationsManager from './ReservationsManager';
import RoomsManager from './RoomsManager';
import UsersManager from './UsersManager';

const TABS = [
    { id: 'requests', label: 'Requests'},
    { id: 'rooms', label: 'Rooms'},
    { id: 'users', label: 'Users'},
];

export default function AdminPanel() {
    const [tab, setTab] = useState('requests');

    return (
        <div>
            <div className="dash-header">
                <h1 className="dash-title">Admin</h1>
                <p className="dash-subtitle">Manage requests, rooms, and users.</p>
            </div>

            <div className="subtabs">
                {TABS.map((t) => (
                  <button
                    key={t.id}
                    type="button"
                    className={`subtab ${tab === t.id ? 'is-active' : ''}`}
                    onClick={() => setTab(t.id)}
                  >
                      {t.label}
                  </button>
                ))}
            </div>

            {tab === 'requests' && <ReservationsManager />}
            {tab === 'rooms' && <RoomsManager />}
            {tab === 'users' && <UsersManager />}
        </div>
    );
}
