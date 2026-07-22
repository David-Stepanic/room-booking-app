import { FiXCircle } from 'react-icons/fi';

export default function Modal({ title, subtitle, onClose, children}) {
    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose();
    };

    return (
        <div className="modal-overlay" onClick={handleOverlayClick}>
            <div className="modal">
                <div className="modal-header">
                    <div>
                        <h3 className="modal-title">{title}</h3>
                        {subtitle && <p className="modal-subtitle">{subtitle}</p>}
                    </div>
                    <button
                        type="button"
                        className="modal-close"
                        onClick={onClose}
                    >
                        <FiXCircle />
                    </button>
                </div>
                {children}
            </div>
        </div>
    );
}