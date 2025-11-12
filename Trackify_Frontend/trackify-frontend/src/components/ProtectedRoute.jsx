import { Navigate } from 'react-router-dom';
import { isAuthed, getRole } from '../auth/useAuth';

export default function ProtectedRoute({ children, roles }) {
  if (!isAuthed()) return <Navigate to="/login" replace />;
  const r = getRole();
  if (roles && !roles.includes(r)) return <Navigate to="/" replace />;
  return children;
}