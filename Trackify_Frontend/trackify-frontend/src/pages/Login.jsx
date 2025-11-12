import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api/client';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [err, setErr] = useState('');
  const nav = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setErr('');
    try {
      const { data } = await api.post('/auth/login', { username, password });
      localStorage.setItem('token', data.token);
      localStorage.setItem('role', data.role);
      localStorage.setItem('username', data.username);
      // Redirect by role
      if (data.role === 'ADMIN') nav('/admin');
      else if (data.role === 'FACULTY') nav('/faculty');
      else nav('/student');
    } catch {
      setErr('Invalid credentials');
    }
  };

  return (
    <div style={{ maxWidth: 380, margin: '40px auto' }}>
      <h2>Login</h2>
      <form onSubmit={submit}>
        <input style={{ display:'block', width:'100%', marginBottom:8 }} placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)} />
        <input style={{ display:'block', width:'100%', marginBottom:8 }} type="password" placeholder="Password" value={password} onChange={(e)=>setPassword(e.target.value)} />
        <button type="submit">Login</button>
      </form>
      {err && <p style={{ color:'red' }}>{err}</p>}
      <p>New user? <Link to="/register">Register</Link></p>
    </div>
  );
}