import { roomTypeLabel } from '../../utils/enums';

export default function RoomCard({ room, onReserve }) {
    return (
        <div className="room-card">
            <span className="room-card-type">{roomTypeLabel(room.roomType)}</span>
            <span className="room-card-number">Room {room.roomNumber}</span>
            <span className="room-card-capacity">Seats up to {room.capacity}</span>
            <button type="button" className="btn btn-primary" onClick={() => onReserve(room)}>
                Reserve
            </button>
        </div>
    );
}