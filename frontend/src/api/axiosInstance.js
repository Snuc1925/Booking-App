import axios from 'axios';
import { store } from '../redux/store';
import { setAccessToken, logout } from '../redux/authSlice';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
});

api.interceptors.request.use((config) => {
  const token = store.getState().auth.accessToken;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (res) => res,
  async (error) => {
    const originalRequest = error.config;
    if (error.response && error.response.status === 403 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        // Lấy refreshToken từ Redux state
        const refreshToken = store.getState().auth.refreshToken;
        if (!refreshToken) {
            store.dispatch(logout());
            window.location.href = '/login';
            return Promise.reject(error);
        }

        const res = await axios.post(
          `${process.env.REACT_APP_API_URL}/auth/refresh-token`,
          { refreshToken }
        );
        
        const newAccessToken = res.data.accessToken;
        store.dispatch(setAccessToken(newAccessToken));
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return api(originalRequest);
      } catch (e) {
        store.dispatch(logout());
        window.location.href = '/login';
        return Promise.reject(e);
      }
    }
    return Promise.reject(error);
  }
);

export default api;