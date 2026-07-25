import { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getAllReservations, cancelOwnReservation } from '../../api/reservationApi';
import { roomTypeLabel } from '../../utils/enums';
import { formatDateTime } from '../../utils/datetime';
import StatusBadge from '../common/StatusBadge';

export default function MyReservations() {
    const { user } = useAuth();
    const [reservations, setReservations] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [busyId, setBusyId] = useState(null);

    const load = () => {
        setLoading(true);
        getAllReservations()
            .then((res) => {
                const mine = res.data.filter((r) => r.email === user?.email);
                setReservations(mine);
            })
            .catch((err) => {
                setError(err.response?.data?.message || 'Could not load your reservations.');
            })
            .finally(() => setLoading(false));
    };

    useEffect(load, [user?.email]);

    const handleCancel = async (id) => {
        setBusyId(id);
        setError('');
        try {
            await cancelOwnReservation(id);
            load();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not cancel the reservation.');
        } finally {
            setBusyId(null);
        }
    };

    return (
        <div>
            <div className="dash-header">
                <h1 className="dash-title">My reservations</h1>
                <p className="dash-subtitle">
                    Everything you've booked, and where it stands.
                </p>
            </div>

            {error && <div className="banner banner--error">{error}</div>}
            {loading && <div className="loading-state">Loading your reservations…</div>}

            {!loading && reservations && reservations.length === 0 && (
                <div className="empty-state">
                    <p className="empty-state-title">Nothing booked yet</p>
                    <p>Head over to Browse &amp; reserve to find a room.</p>
                </div>
            )}

            {!loading && reservations && reservations.length > 0 && (
                <div className="table-wrap">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Title</th>
                            <th>Room</th>
                            <th>When</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {reservations.map((r) => (
                            <tr key={r.id}>
                                <td>{r.title || '—'}</td>
                                <td>
                                    Room {r.roomNumber}
                                    <div className="cell-muted">{roomTypeLabel(r.roomType)}</div>
                                </td>
                                <td className="cell-muted">
                                    {formatDateTime(r.startTime)} → {formatDateTime(r.endTime)}
                                </td>
                                <td>
                                    <StatusBadge status={r.reservationStatus} />
                                    {r.reservationStatus === 'DECLINED' && r.declinedReason && (
                                        <div className="cell-muted" style={{ marginTop: 4 }}>
                                            {r.declinedReason}
                                        </div>
                                    )}
                                </td>
                                <td>
                                    {r.reservationStatus === 'PENDING' && (
                                        <button
                                            type="button"
                                            className="btn btn-danger btn-sm"
                                            disabled={busyId === r.id}
                                            onClick={() => handleCancel(r.id)}
                                        >
                                            {busyId === r.id ? 'Canceling…' : 'Cancel'}
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}