import { reservationStatusLabel, statusBadgeClass } from '../../utils/enums.js';

export default function StatusBadge({ status }) {
    return <span className={statusBadgeClass(status)}>{reservationStatusLabel(status)}</span>;
}
