import { useEffect, useState } from 'react';
import api from '../api/client';

export default function Attendance() {
  const [records, setRecords] = useState([]);
  useEffect(() => {
    api.get('/attendance/get-all-attendance-records').then(res => setRecords(res.data || []));
  }, []);
  return (
    <div style={{ padding:16 }}>
      <h3>Attendance Records</h3>
      <ul>
        {records.map(r => (
          <li key={r.id}>
            {r.date} {r.time} | Subject: {r.subject?.name || r.subject?.id} | Students: {r.numberOfStudents}
          </li>
        ))}
      </ul>
    </div>
  );
}