import { Link } from 'react-router-dom';
import { getRole, logout } from '../auth/useAuth';

export default function Layout({ children }) {
  const role = getRole();
  return (
    <div style={{ minHeight:'100vh', background:'linear-gradient(180deg,#0f172a 0%, #111827 100%)', color:'#e5e7eb' }}>
      <nav style={{ display:'flex', alignItems:'center', justifyContent:'space-between', padding:'12px 20px', borderBottom:'1px solid #1f2937', position:'sticky', top:0, backdropFilter:'blur(6px)' }}>
        <div style={{ display:'flex', gap:16, alignItems:'center' }}>
          <Link to="/" style={{ color:'#93c5fd', fontWeight:700, letterSpacing:0.5 }}>Trackify</Link>
          <Link to="/subjects" style={{ color:'#9ca3af' }}>Subjects</Link>
          <Link to="/students" style={{ color:'#9ca3af' }}>Students</Link>
          <Link to="/attendance" style={{ color:'#9ca3af' }}>Attendance</Link>
          {(role === 'ADMIN' || role === 'FACULTY') && (
            <Link to="/take-attendance" style={{ color:'#9ca3af' }}>Take Attendance</Link>
          )}
          {role === 'ADMIN' && (
            <Link to="/admin/users" style={{ color:'#fca5a5', fontWeight:600 }}>Admin</Link>
          )}
          {role === 'STUDENT' && (
            <Link to="/student/profile" style={{ color:'#a7f3d0' }}>My Profile</Link>
          )}
          {(role === 'FACULTY' || role === 'ADMIN') && (
            <Link to="/faculty/profile" style={{ color:'#fde68a' }}>Faculty Profile</Link>
          )}
        </div>
        <div style={{ display:'flex', gap:12, alignItems:'center' }}>
          <span style={{ fontSize:12, color:'#9ca3af', padding:'2px 8px', border:'1px solid #374151', borderRadius:999 }}>{role || 'GUEST'}</span>
          <button onClick={logout} style={{ background:'#ef4444', color:'#fff', border:'none' }}>Logout</button>
        </div>
      </nav>
      <main style={{ maxWidth:1100, margin:'24px auto', padding:'0 20px' }}>
        {children}
      </main>
    </div>
  );
}


