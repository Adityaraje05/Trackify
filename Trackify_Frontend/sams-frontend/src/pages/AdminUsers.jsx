import { useEffect, useState } from 'react';
import api from '../api/client';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/user/get-all-user');
      setUsers(res.data || []);
    } catch (e) {
      setError(e?.response?.data || 'Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const updateRole = async (username, roleName) => {
    try {
      // Fetch the user first to retain other fields
      const u = users.find(x => x.username === username);
      if (!u) return;
      const payload = {
        ...u,
        role: { id: u.role?.id, name: roleName } // backend expects Role object on update
      };
      await api.put('/user/update-user', payload);
      await load();
    } catch (e) {
      setError(e?.response?.data || 'Failed to update role');
    }
  };

  const remove = async (username) => {
    if (!confirm(`Delete user ${username}?`)) return;
    try {
      await api.delete('/user/delete-user-by-username', { params: { username } });
      await load();
    } catch (e) {
      setError(e?.response?.data || 'Failed to delete user');
    }
  };

  return (
    <div>
      <h2 style={{ margin:'8px 0 16px 0' }}>Admin • Users</h2>
      {error && <div style={{ marginBottom:12, padding:12, background:'#4b5563', color:'#fecaca', border:'1px solid #ef4444' }}>{error}</div>}
      {loading ? (
        <p>Loading users...</p>
      ) : (
        <div style={{ display:'grid', gridTemplateColumns:'repeat(auto-fill,minmax(280px,1fr))', gap:16 }}>
          {users.map(u => (
            <div key={u.username} style={{ padding:16, border:'1px solid #1f2937', borderRadius:12, background:'#0b1220' }}>
              <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:8 }}>
                <strong style={{ color:'#f3f4f6' }}>{u.firstName} {u.lastName}</strong>
                <span style={{ fontSize:12, color:'#9ca3af' }}>{u.username}</span>
              </div>
              <div style={{ fontSize:13, color:'#9ca3af', marginBottom:12 }}>{u.email}</div>
              <div style={{ display:'flex', gap:8, alignItems:'center', marginBottom:12 }}>
                <label style={{ fontSize:12, color:'#9ca3af' }}>Role</label>
                <select value={u.role?.name || 'STUDENT'} onChange={(e)=>updateRole(u.username, e.target.value)} style={{ padding:'6px 8px', background:'#0f172a', color:'#e5e7eb', border:'1px solid #374151', borderRadius:6 }}>
                  <option value="ADMIN">ADMIN</option>
                  <option value="FACULTY">FACULTY</option>
                  <option value="STUDENT">STUDENT</option>
                </select>
              </div>
              <div style={{ display:'flex', justifyContent:'space-between' }}>
                <button onClick={()=>updateRole(u.username, u.role?.name || 'STUDENT')} style={{ background:'#2563eb', color:'#fff', border:'none' }}>Save</button>
                <button onClick={()=>remove(u.username)} style={{ background:'#ef4444', color:'#fff', border:'none' }}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}


