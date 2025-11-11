import { Link } from 'react-router-dom';
import { getRole, logout } from '../auth/useAuth';

export default function Dashboard() {
  const role = getRole() || 'STUDENT';

  return (
    <div style={{ padding:16 }}>
      <h2>Trackify Dashboard ({role})</h2>
      <nav style={{ display:'flex', gap:10, marginBottom:16 }}>
        <Link to="/subjects">Subjects</Link>
        {(role === 'ADMIN' || role === 'FACULTY') && <Link to="/students">Students</Link>}
        <Link to="/attendance">Attendance</Link>
        {(role === 'ADMIN' || role === 'FACULTY') && <Link to="/take-attendance">Take Attendance</Link>}
        <button onClick={logout}>Logout</button>
      </nav>
      <p>Welcome! Use the navigation above.</p>
    </div>
  );
}