import { useEffect, useState } from 'react';
import api from '../api/client';

export default function Students() {
  const [list, setList] = useState([]);
  const [form, setForm] = useState({ name:'', email:'' });
  const [msg, setMsg] = useState('');
  const load = async () => {
    try {
      const { data } = await api.get('/student/get-all-students');
      setList(data || []);
    } catch {
      setMsg('Failed to load students');
    }
  };
  useEffect(() => { load(); }, []);
  const add = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      await api.post('/student/add-student', form);
      setForm({ name:'', email:'' });
      await load();
      setMsg('Student added.');
    } catch {
      setMsg('Failed to add student.');
    }
  };
  const remove = async (id) => {
    if (!confirm('Delete this student?')) return;
    setMsg('');
    try {
      await api.delete(`/student/delete-student/${id}`);
      // Optimistic remove from UI
      setList(prev => prev.filter(s => s.id !== id));
      setMsg('Student deleted.');
    } catch (e) {
      setMsg('Failed to delete student.');
    }
  };

  return (
    <div style={{ padding:16 }}>
      <h3>Students</h3>
      {msg && <p style={{ color:'#999' }}>{msg}</p>}
      <ul>
        {list.map(s => (
          <li key={s.id} style={{ display:'flex', alignItems:'center', gap:8 }}>
            <span>{s.name} ({s.email})</span>
            <button onClick={()=>remove(s.id)} style={{ marginLeft:8, background:'#ef4444', color:'#fff', border:'none' }}>Delete</button>
          </li>
        ))}
      </ul>
      <form onSubmit={add} style={{ marginTop:16 }}>
        <input value={form.name} onChange={(e)=>setForm({ ...form, name:e.target.value })} placeholder="Name" />
        <input value={form.email} onChange={(e)=>setForm({ ...form, email:e.target.value })} placeholder="Email" style={{ marginLeft:8 }} />
        <button type="submit" style={{ marginLeft:8 }}>Add</button>
      </form>
    </div>
  );
}