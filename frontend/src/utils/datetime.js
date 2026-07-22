// extend date-local time with seconds
export function toApiDateTime(inputValue) {
    if (!inputValue) return null;
    return inputValue.length === 16 ? `${inputValue}:00` : inputValue;
}

// Format an ISO/LocalDateTime string for display, e.g. "Jul 9, 2:30 PM"
export function formatDateTime(value) {
    if (!value) return '-';
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return value;
    return date.toLocaleString(undefined, {
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
    });
}

// Default start = next full hour, end = one hour after that,
// formatted for a datetime-local input's value prop.
export function defaultRange() {
    const start = new Date();
    start.setMinutes(0, 0, 0);
    start.setHours(start.getHours() + 1);

    const end = new Date(start);
    end.setHours(end.getHours() + 1);

    return {
        start: toDateTimeLocalValue(start),
        end: toDateTimeLocalValue(end),
    };
}

export function toDateTimeLocalValue(date) {
    const pad = (n) => String(n).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
}