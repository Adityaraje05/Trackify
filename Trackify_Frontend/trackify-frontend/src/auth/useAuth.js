import { jwtDecode } from 'jwt-decode';

export function getToken() {
  return localStorage.getItem('token');
}

export function getRole() {
  try {
    const t = getToken();
    if (!t) return null;
    const payload = jwtDecode(t);
    return payload.role || payload['role'] || localStorage.getItem('role') || null;
  } catch {
    return null;
  }
}

export function isAuthed() {
  return !!getToken();
}

export function logout() {
  localStorage.clear();
  window.location.href = '/login';
}