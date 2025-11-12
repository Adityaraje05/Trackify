import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || 'http://localhost:8091',
  timeout: 10000
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err?.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    } else if (err?.code === 'ECONNABORTED' || err?.message?.includes('Network Error')) {
      console.error('Backend connection error. Make sure the backend is running on http://localhost:8091');
    } else {
      console.error('API Error:', err?.response?.data || err?.message);
    }
    return Promise.reject(err);
  }
);

export default api;

