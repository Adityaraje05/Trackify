import { useEffect, useState } from 'react';
import api from '../api/client';

export default function TakeAttendance() {
  const [subjects, setSubjects] = useState([]);
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState({
    username: localStorage.getItem('username') || '',
    subjectId: '',
    date: '',
    time: '',
    studentIds: []
  });
  const [msg, setMsg] = useState('');

  useEffect(() => {
    api.get('/subject/get-all-subjects').then(r => setSubjects(r.data || []));
    api.get('/student/get-all-students').then(r => setStudents(r.data || []));
  }, []);

  const toggleStudent = (id) => {
    setForm((prev) => {
      const has = prev.studentIds.includes(id);
      return { ...prev, studentIds: has ? prev.studentIds.filter(x => x !== id) : [...prev.studentIds, id] };
    });
  };

  const submit = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      const payload = {
        username: form.username,
        subjectId: form.subjectId ? Number(form.subjectId) : null,
        date: form.date,
        time: form.time,
        studentIds: form.studentIds
      };
      await api.post('/attendance/take-attendance', payload);
      setMsg('Attendance saved.');
    } catch (err) {
      setMsg('Failed to save attendance.');
    }
  };

  return (
    <div style={{ padding:16 }}>
      <h3>Take Attendance</h3>
      <form onSubmit={submit}>
        <div style={{ marginBottom:8 }}>
          <label>Faculty Username:</label>
          <input value={form.username} onChange={(e)=>setForm({ ...form, username:e.target.value })} style={{ marginLeft:8 }} />
        </div>
        <div style={{ marginBottom:8 }}>
          <label>Subject:</label>
          <select value={form.subjectId} onChange={(e)=>setForm({ ...form, subjectId:e.target.value })} style={{ marginLeft:8 }}>
            <option value="">Select subject</option>
            {subjects.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
        </div>
        <div style={{ marginBottom:8 }}>
          <label>Date:</label>
          <input type="date" value={form.date} onChange={(e)=>setForm({ ...form, date:e.target.value })} style={{ marginLeft:8 }} />
        </div>
        <div style={{ marginBottom:8 }}>
          <label>Time:</label>
          <input type="time" value={form.time} onChange={(e)=>setForm({ ...form, time:e.target.value })} style={{ marginLeft:8 }} />
        </div>
        <div style={{ marginBottom:8 }}>
          <label>Students:</label>
          <button type="button" onClick={()=>api.get('/student/get-all-students').then(r => setStudents(r.data || []))} style={{ marginLeft:8 }}>Refresh</button>
          <div style={{ display:'grid', gridTemplateColumns:'repeat(3, 1fr)', gap:6, marginTop:8 }}>
            {students.map(st => (
              <label key={st.id} style={{ border:'1px solid #ccc', padding:6 }}>
                <input
                  type="checkbox"
                  checked={form.studentIds.includes(st.id)}
                  onChange={()=>toggleStudent(st.id)}
                />
                <span style={{ marginLeft:6 }}>{st.name}</span>
              </label>
            ))}
          </div>
        </div>
        <button type="submit">Save Attendance</button>
      </form>
      {msg && <p style={{ marginTop:10 }}>{msg}</p>}
    </div>
  );
}