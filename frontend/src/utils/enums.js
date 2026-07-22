export const ROOM_TYPE_LABELS = {
    COMPUTER_ROOM: 'Computer Room',
    TEACHING_ROOM: 'Teaching Room',
    COMPUTER_CENTER: 'Computer Center',
    AMPHITHEATER: 'Amphitheater',
};

export const ROOM_TYPE_OPTIONS = Object.entries(ROOM_TYPE_LABELS).map(([value, label]) => ({
    value,
    label
}));

export const RESERVATION_STATUS_LABELS = {
    PENDING: 'Pending',
    CONFIRMED: 'Confirmed',
    DECLINED: 'Declined',
    CANCELED: 'Canceled',
};


export const DEPARTMENT_LABELS = {
    SOFTWARE_ENGINEERING: 'Software Engineering',
    MARKETING: 'Marketing',
    ARTIFICIAL_INTELLIGENCE: 'Artificial Intelligence',
    MANAGEMENT: 'Management',
    HUMAN_RESOURCES: 'Human Resources',
};

export function roomTypeLabel(type) {
    return ROOM_TYPE_LABELS[type] || type;
}

export function reservationStatusLabel(status) {
    return RESERVATION_STATUS_LABELS[status] || status;
}

export function departmentLabel(dept) {
    return DEPARTMENT_LABELS[dept] || dept;
}

export function statusBadgeClass(status) {
    switch (status) {
        case 'CONFIRMED':
            return 'status-badge status-badge--confirmed';
        case 'DECLINED':
            return 'status-badge status-badge--declined';
        case 'CANCELED':
            return 'status-badge status-badge--canceled';
        case 'PENDING':
        default:
            return 'status-badge status-badge--pending';
    }
}