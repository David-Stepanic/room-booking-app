import { useState } from 'react';
import { getAvailableRooms } from '../../api/roomApi';
import { toApiDateTime, defaultRange } from '../../utils/datetime';
import RoomCard from './RoomCard';
import ReservationModal from '../reservations/ReservationModal';


export default function RoomBrowser() {
    const initialRange = defaultRange();
    const [startTime, setStartTime] = useState(initialRange.start);
    const [endTime, setEndTime] = useState(initialRange.end);
    const [rooms, setRooms] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [selectedRoom, setSelectedRoom] = useState(null);
    const [successMessage, setSuccessMessage] = useState('');

    const search = async (e) => {
        e?.preventDefault();
        setError('');
        setSuccessMessage('');
        setLoading(true);

        try {
            const response = await getAvailableRooms(toApiDateTime(startTime), toApiDateTime(endTime));
            setRooms(response.data);
        } catch (err) {
            setError(err.response?.data?.message || 'Could not load available rooms.');
            setRooms([]);
        } finally {
            setLoading(false);
        }
    };

    const handleCreated = () => {
        setSelectedRoom(null);
        setSuccessMessage('Reservation submitted - an admin will confirm it shortly.');
        search();
    }

    return (
        <div>
            <div className="dash-header">
                <h1 className="dash-title">Browse & Reserve</h1>
                <p className="dash-subtitle">Pick a time range to see which rooms are free.</p>
            </div>

            {successMessage && <div className="banner banner-success">{successMessage}</div>}
            {error && <div className="banner banner-error">{error}</div>}

            <form className="search-bar panel" onSubmit={search}>
                <div className="search-field">
                    <label htmlFor="search-start">Start</label>
                    <input
                        id="search-start"
                        type="datetime-local"
                        value={startTime}
                        onChange={(e) => setStartTime(e.target.value)}
                        required
                    />
                </div>
                <div className="search-field">
                    <label htmlFor="search-end">End</label>
                    <input
                        id="search-end"
                        type="datetime-local"
                        value={endTime}
                        onChange={(e) => setEndTime(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Searching…' : 'Search rooms'}
                </button>
            </form>

            {rooms === null && !loading && (
                <div className="empty-state">
                    <p className="empty-state-title">No search yet</p>
                    <p>Choose a start and end time above to see available rooms.</p>
                </div>
            )}

            {loading && <div className="loading-state">Looking for open rooms...</div>}

            {rooms !== null && !loading && rooms.length === 0 && (
                <div className="empty-state">
                    <p className="empty-state-title">Nothing free in that window</p>
                    <p>Try a different time range.</p>
                </div>
            )}

            {rooms !== null && !loading && rooms.length > 0 && (
                <div className="room-grid">
                    {rooms.map((room) => (
                        <RoomCard key={room.id} room={room} onReserve={setSelectedRoom} />
                    ))}
                </div>
            )}

            {selectedRoom && (
                <ReservationModal
                    room={selectedRoom}
                    startTime={startTime}
                    endTime={endTime}
                    onClose={() => setSelectedRoom(null)}
                    onCreated={handleCreated}
                />
            )}
        </div>
    )
}