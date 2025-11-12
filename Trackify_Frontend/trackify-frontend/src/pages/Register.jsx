import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api/client';

export default function Register() {
  const [form, setForm] = useState({ username:'', password:'', firstName:'', lastName:'', email:'', roleName:'STUDENT' });
  const [err, setErr] = useState('');
  const nav = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setErr('');
    
    // Frontend validation
    if (!form.username || !form.password || !form.firstName || !form.lastName || !form.email) {
      setErr('Please fill in all fields');
      return;
    }
    
    if (form.password.length < 6) {
      setErr('Password must be at least 6 characters');
      return;
    }
    
    // Basic email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) {
      setErr('Please enter a valid email address');
      return;
    }
    
    try {
      console.log('Sending registration data:', form);
      const response = await api.post('/auth/register', form);
      console.log('Registration successful:', response.data);
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('role', response.data.role);
      localStorage.setItem('username', response.data.username);
      nav('/');
    } catch (err) {
      console.error('Registration error:', err);
      console.error('Error response:', err?.response);
      const errorMessage = err?.response?.data || err?.message || 'Registration failed (username may already exist)';
      setErr(typeof errorMessage === 'string' ? errorMessage : JSON.stringify(errorMessage));
    }
  };

  return (
    <div style={{ maxWidth: 420, margin: '40px auto' }}>
      <h2>Register</h2>
      <form onSubmit={submit}>
        <input style={{ display:'block', width:'100%', marginBottom:8 }} placeholder="Username" value={form.username} onChange={(e)=>setForm({ ...form, username:e.target.value })} />
        <input style={{ display:'block', width:'100%', marginBottom:8 }} type="password" placeholder="Password" value={form.password} onChange={(e)=>setForm({ ...form, password:e.target.value })} />
        <input style={{ display:'block', width:'100%', marginBottom:8 }} placeholder="First name" value={form.firstName} onChange={(e)=>setForm({ ...form, firstName:e.target.value })} />
        <input style={{ display:'block', width:'100%', marginBottom:8 }} placeholder="Last name" value={form.lastName} onChange={(e)=>setForm({ ...form, lastName:e.target.value })} />
        <input style={{ display:'block', width:'100%', marginBottom:8 }} placeholder="Email" value={form.email} onChange={(e)=>setForm({ ...form, email:e.target.value })} />
        <select style={{ display:'block', width:'100%', marginBottom:8 }} value={form.roleName} onChange={(e)=>setForm({ ...form, roleName:e.target.value })}>
          <option value="STUDENT">Student</option>
          <option value="FACULTY">Faculty</option>
          <option value="ADMIN">Admin</option>
        </select>
        <button type="submit">Create account</button>
      </form>
      {err && <p style={{ color:'red' }}>{err}</p>}
      <p>Have an account? <Link to="/login">Login</Link></p>
    </div>
  );
}