import { useEffect, useState } from 'react';
import api from '../api/client';

export default function Subjects() {
  const [list, setList] = useState([]);
  const [name, setName] = useState('');
  const load = async () => {
    const { data } = await api.get('/subject/get-all-subjects');
    setList(data || []);
  };
  useEffect(() => { load(); }, []);
  const add = async (e) => {
    e.preventDefault();
    if (!name.trim()) return;
    await api.post('/subject/add-subject', { name });
    setName('');
    await load();
  };

  return (
    <div style={{ padding:16 }}>
      <h3>Subjects</h3>
      <ul>{list.map(s => <li key={s.id}>{s.name}</li>)}</ul>
      <form onSubmit={add} style={{ marginTop:16 }}>
        <input value={name} onChange={(e)=>setName(e.target.value)} placeholder="New subject name" />
        <button type="submit" style={{ marginLeft:8 }}>Add</button>
      </form>
    </div>
  );
}