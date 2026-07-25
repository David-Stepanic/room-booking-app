import { useEffect, useState } from 'react';
import {
    getAllReservations,
    confirmReservation,
    declineReservation,
    deleteReservation,
} from '../../api/reservationApi';
import { roomTypeLabel } from '../../utils/enums';
import { formatDateTime } from '../../utils/datetime';
import StatusBadge from '../common/StatusBadge';
import DeclineModal from './DeclineModal';
import { RESERVATION_STATUS_LABELS } from '../../utils/enums';
import { FiArrowRight } from 'react-icons/fi';

export default function ReservationsManager() {
    const [reservations, setReservations] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [decliningId, setDecliningId] = useState(null);
    const [busyId, setBusyId] = useState(null);
    const [filter, setFilter] = useState('PENDING');

    const load = () => {
        setLoading(true);
        getAllReservations()
            .then((res) => setReservations(res.data))
            .catch((err) => setError(err.response?.data?.message || 'Could not load reservations.'))
            .finally(() => setLoading(false));
    };

    useEffect(load, []);

    const handleConfirm = async (id) => {
        setBusyId(id);
        setError('');
        try {
            await confirmReservation(id);
            load();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not confirm reservation.');
        } finally {
            setBusyId(null);
        }
    };

    const handleDecline = async (reason) => {
        await declineReservation(decliningId, { reason });
        setDecliningId(null);
        load();
    };

    const handleDelete = async (id) => {
        setBusyId(id);
        setError('');
        try {
            await deleteReservation(id);
            load();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not delete reservation.');
        } finally {
            setBusyId(null);
        }
    };

    const visible = reservations?.filter((r) => (filter === 'ALL' ? true : r.reservationStatus === filter));

    return (
        <div>
            <div className="dash-header">
                <h1 className="dash-title">Reservation requests</h1>
                <p className="dash-subtitle">Confirm, decline, or remove booking requests.</p>
            </div>

            {error && <div className="banner banner--error">{error}</div>}

            <div className="subtabs">
                {[...Object.keys(RESERVATION_STATUS_LABELS), 'ALL'].map((s) => (
                    <button
                        key={s}
                        type="button"
                        className={`subtab ${filter === s ? 'is-active' : ''}`}
                        onClick={() => setFilter(s)}
                    >
                        {s === 'ALL' ? 'All' : RESERVATION_STATUS_LABELS[s]}
                    </button>
                ))}
            </div>

            {loading && <div className="loading-state">Loading reservations…</div>}

            {!loading && visible && visible.length === 0 && (
                <div className="empty-state">
                    <p className="empty-state-title">Nothing here</p>
                    <p>No reservations match this filter.</p>
                </div>
            )}

            {!loading && visible && visible.length > 0 && (
                <div className="table-wrap">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Requested by</th>
                            <th>Title</th>
                            <th>Room</th>
                            <th>When</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {visible.map((r) => (
                            <tr key={r.id}>
                                <td>{r.email}</td>
                                <td>{r.title || '—'}</td>
                                <td>
                                    Room {r.roomNumber}
                                    <div className="cell-muted">{roomTypeLabel(r.roomType)}</div>
                                </td>
                                <td className="cell-muted">
                                    {formatDateTime(r.startTime)} <FiArrowRight /> {formatDateTime(r.endTime)}
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
                                    <div className="table-actions">
                                        {r.reservationStatus === 'PENDING' && (
                                            <>
                                                <button
                                                    type="button"
                                                    className="btn btn-confirm btn-sm"
                                                    disabled={busyId === r.id}
                                                    onClick={() => handleConfirm(r.id)}
                                                >
                                                    Confirm
                                                </button>
                                                <button
                                                    type="button"
                                                    className="btn btn-danger btn-sm"
                                                    disabled={busyId === r.id}
                                                    onClick={() => setDecliningId(r.id)}
                                                >
                                                    Decline
                                                </button>
                                            </>
                                        )}
                                        <button
                                            type="button"
                                            className="btn btn-ghost btn-sm"
                                            disabled={busyId === r.id}
                                            onClick={() => handleDelete(r.id)}
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}

            {decliningId && (
                <DeclineModal onClose={() => setDecliningId(null)} onSubmit={handleDecline} />
            )}
        </div>
    );
}