import { useEffect, useRef, useState } from 'react';
import api from '../api/client';
import { getRole } from '../auth/useAuth';

export default function FacultyProfile() {
  const role = getRole();
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [lastUpdated, setLastUpdated] = useState(null);
  const timerRef = useRef(null);

  const loadStudents = async () => {
    try {
      setError('');
      const res = await api.get('/student/get-all-students');
      setStudents(res.data || []);
      setLastUpdated(new Date());
    } catch (e) {
      setError(e?.response?.data || 'Failed to load students');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStudents();
    // Auto-refresh every 6 seconds so newly added students show up fast
    timerRef.current = setInterval(loadStudents, 6000);
    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, []);

  return (
    <div>
      <h2 style={{ marginBottom: 12 }}>Faculty Profile</h2>
      <div style={{ display:'flex', gap:12, alignItems:'center', marginBottom:12 }}>
        <span style={{ fontSize:12, color:'#9ca3af' }}>Role: {role}</span>
        <button onClick={loadStudents} style={{ background:'#2563eb', color:'#fff', border:'none' }}>Refresh</button>
        {lastUpdated && <span style={{ fontSize:12, color:'#9ca3af' }}>Updated: {lastUpdated.toLocaleTimeString()}</span>}
      </div>
      {error && <div style={{ marginBottom:12, padding:12, background:'#4b5563', color:'#fecaca', border:'1px solid #ef4444' }}>{error}</div>}
      {loading ? (
        <p>Loading students...</p>
      ) : (
        <div>
          <div style={{ marginBottom:8, color:'#9ca3af' }}>Total students: {students.length}</div>
          <div style={{ display:'grid', gridTemplateColumns:'repeat(auto-fill,minmax(260px,1fr))', gap:12 }}>
            {students.map(s => (
              <div key={s.id} style={{ padding:14, border:'1px solid #1f2937', borderRadius:10, background:'#0b1220' }}>
                <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center' }}>
                  <div>
                    <div style={{ fontWeight:600 }}>{s.name}</div>
                    <div style={{ fontSize:13, color:'#9ca3af' }}>{s.email}</div>
                  </div>
                  <button
                    onClick={async()=>{
                      try {
                        await api.delete(`/student/delete-student/${s.id}`);
                        // Optimistic update
                        setStudents(prev => prev.filter(x => x.id !== s.id));
                        setLastUpdated(new Date());
                      } catch (e) {
                        setError(e?.response?.data || 'Failed to delete student');
                      }
                    }}
                    style={{ background:'#ef4444', color:'#fff', border:'none' }}
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
            {!students.length && <div style={{ color:'#9ca3af' }}>No students yet.</div>}
          </div>
        </div>
      )}
    </div>
  );
}


