import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Subjects from './pages/Subjects';
import Students from './pages/Students';
import Attendance from './pages/Attendance';
import TakeAttendance from './pages/TakeAttendance';
import AdminUsers from './pages/AdminUsers';
import AdminDashboard from './pages/AdminDashboard';
import FacultyDashboard from './pages/FacultyDashboard';
import StudentDashboard from './pages/StudentDashboard';
import StudentProfile from './pages/StudentProfile';
import FacultyProfile from './pages/FacultyProfile';

export default function App() {
  return (
    <Routes>
        <Route path="/login" element={<Login />}/>
        <Route path="/register" element={<Register />}/>
        <Route path="/" element={<ProtectedRoute><Layout><Dashboard /></Layout></ProtectedRoute>} />
        <Route path="/admin" element={<ProtectedRoute roles={['ADMIN']}><Layout><AdminDashboard /></Layout></ProtectedRoute>} />
        <Route path="/faculty" element={<ProtectedRoute roles={['FACULTY','ADMIN']}><Layout><FacultyDashboard /></Layout></ProtectedRoute>} />
        <Route path="/student" element={<ProtectedRoute roles={['STUDENT','ADMIN']}><Layout><StudentDashboard /></Layout></ProtectedRoute>} />
        <Route path="/student/profile" element={<ProtectedRoute roles={['STUDENT','ADMIN']}><Layout><StudentProfile /></Layout></ProtectedRoute>} />
        <Route path="/faculty/profile" element={<ProtectedRoute roles={['FACULTY','ADMIN']}><Layout><FacultyProfile /></Layout></ProtectedRoute>} />
        <Route path="/subjects" element={<ProtectedRoute roles={['ADMIN','FACULTY','STUDENT']}><Layout><Subjects /></Layout></ProtectedRoute>} />
        <Route path="/students" element={<ProtectedRoute roles={['ADMIN','FACULTY']}><Layout><Students /></Layout></ProtectedRoute>} />
        <Route path="/attendance" element={<ProtectedRoute roles={['ADMIN','FACULTY','STUDENT']}><Layout><Attendance /></Layout></ProtectedRoute>} />
        <Route path="/take-attendance" element={<ProtectedRoute roles={['ADMIN','FACULTY']}><Layout><TakeAttendance /></Layout></ProtectedRoute>} />
        <Route path="/admin/users" element={<ProtectedRoute roles={['ADMIN']}><Layout><AdminUsers /></Layout></ProtectedRoute>} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
  );
}