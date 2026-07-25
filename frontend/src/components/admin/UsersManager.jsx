import { useEffect, useState } from 'react';
import { getUsers, deleteUser } from '../../api/userApi';
import { useAuth } from '../../context/AuthContext';
import { departmentLabel } from '../../utils/enums';

export default function UsersManager() {
    const { user: currentUser } = useAuth();
    const [users, setUsers] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [busyId, setBusyId] = useState(null);

    const load = () => {
        setLoading(true);
        getUsers()
            .then((res) => setUsers(res.data))
            .catch((err) => setError(err.response?.data?.message || 'Could not load users.'))
            .finally(() => setLoading(false));
    };

    useEffect(load, []);

    const handleDelete = async (id) => {
        setBusyId(id);
        setError('');
        try {
            await deleteUser(id);
            load();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not delete user.');
        } finally {
            setBusyId(null);
        }
    };

    return (
        <div>
            <div className="dash-header" style={{ marginBottom: '24px' }}>
                <h1 className="dash-title">Users</h1>
                <p className="dash-subtitle">Everyone with access to this workspace.</p>
            </div>

            {error && <div className="banner banner--error">{error}</div>}
            {loading && <div className="loading-state">Loading users…</div>}

            {!loading && users && users.length > 0 && (
                <div className="table-wrap">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Department</th>
                            <th>Role</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map((u) => {
                            const isSelf = u.email === currentUser?.email;
                            return (
                                <tr key={u.id}>
                                    <td>{u.firstName} {u.lastName}</td>
                                    <td className="cell-muted">{u.email}</td>
                                    <td>{departmentLabel(u.department)}</td>
                                    <td>{u.role}</td>
                                    <td>
                                        <div className="table-actions">
                                            <button
                                                type="button"
                                                className="btn btn-danger btn-sm"
                                                disabled={busyId === u.id || isSelf}
                                                title={isSelf ? "You can't delete your own account" : undefined}
                                                onClick={() => handleDelete(u.id)}
                                            >
                                                Delete
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}