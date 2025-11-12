import { useEffect, useMemo, useState } from 'react';
import api from '../api/client';

export default function StudentProfile() {
  const [student, setStudent] = useState(null);
  const [records, setRecords] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const loadMe = async () => {
    try {
      const res = await api.get('/student/me');
      setStudent(res.data || null);
    } catch (e) {
      setError(e?.response?.data || 'Failed to load profile');
    }
  };

  const loadAttendance = async (id) => {
    if (!id) return;
    setLoading(true);
    setError('');
    try {
      const res = await api.get(`/attendance/by-student/${id}`);
      setRecords(res.data || []);
    } catch (e) {
      setError(e?.response?.data || 'Failed to load attendance');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadMe(); }, []);

  useEffect(() => {
    if (student?.id) loadAttendance(student.id);
  }, [student?.id]);

  const summary = useMemo(() => {
    const total = records.length;
    const bySubject = {};
    for (const r of records) {
      const key = r.subject?.name || `Subject #${r.subject?.id || '-'}`;
      bySubject[key] = (bySubject[key] || 0) + 1;
    }
    return { total, bySubject };
  }, [records]);

  return (
    <div>
      <h2 style={{ marginBottom: 12 }}>My Profile</h2>
      {student && (
        <div style={{ marginBottom: 12, color:'#9ca3af' }}>
          {student.name} ({student.email})
        </div>
      )}
      {error && <div style={{ marginBottom:12, padding:12, background:'#4b5563', color:'#fecaca', border:'1px solid #ef4444' }}>{error}</div>}
      {loading ? <p>Loading attendance...</p> : (
        <>
          <div style={{ display:'flex', gap:16, flexWrap:'wrap', marginBottom:16 }}>
            <div style={{ padding:16, border:'1px solid #1f2937', borderRadius:12, background:'#0b1220', minWidth:220 }}>
              <div style={{ color:'#9ca3af', fontSize:12, marginBottom:6 }}>Total Sessions Attended</div>
              <div style={{ fontSize:28, fontWeight:700 }}>{summary.total}</div>
            </div>
            <div style={{ padding:16, border:'1px solid #1f2937', borderRadius:12, background:'#0b1220', minWidth:260 }}>
              <div style={{ color:'#9ca3af', fontSize:12, marginBottom:6 }}>By Subject</div>
              <ul style={{ margin:0, paddingLeft:16 }}>
                {Object.entries(summary.bySubject).map(([k,v]) => (
                  <li key={k} style={{ color:'#e5e7eb' }}>{k}: {v}</li>
                ))}
                {!Object.keys(summary.bySubject).length && <li style={{ color:'#9ca3af' }}>No data</li>}
              </ul>
            </div>
          </div>
          <div>
            <h3 style={{ margin:'12px 0' }}>Attendance Records</h3>
            <div style={{ display:'grid', gridTemplateColumns:'repeat(auto-fill,minmax(300px,1fr))', gap:12 }}>
              {records.map(r => (
                <div key={`${r.date}-${r.time}-${r.subject?.id}`} style={{ padding:14, border:'1px solid #1f2937', borderRadius:10, background:'#0b1220' }}>
                  <div style={{ fontWeight:600 }}>{r.subject?.name || `Subject #${r.subject?.id}`}</div>
                  <div style={{ color:'#9ca3af', fontSize:13 }}>{r.date} • {r.time}</div>
                  <div style={{ color:'#9ca3af', fontSize:12, marginTop:6 }}>Class size: {r.numberOfStudents}</div>
                </div>
              ))}
              {!records.length && <div style={{ color:'#9ca3af' }}>No attendance found.</div>}
            </div>
          </div>
        </>
      )}
    </div>
  );
}


