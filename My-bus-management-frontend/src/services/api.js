import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    } else {
      console.warn('No token found in localStorage for request:', config.url);
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Handle 401 errors (unauthorized)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Only redirect if we're not already on the login page
      if (!window.location.pathname.includes('/login')) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// Bus APIs
export const busAPI = {
  getAllBuses: () => api.get('/buses'),
  getBusById: (id) => api.get(`/buses/${id}`),
  searchBuses: (searchData) => api.post('/buses/search', searchData),
  createBus: (busData) => api.post('/buses', busData),
  updateBus: (id, busData) => api.put(`/buses/${id}`, busData),
  deleteBus: (id) => api.delete(`/buses/${id}`),
};

// Auth APIs
export const authAPI = {
  login: (loginData) => api.post('/auth/login', loginData),
  signup: (signupData) => api.post('/auth/signup', signupData),
};

// Booking APIs
export const bookingAPI = {
  createBooking: (bookingData) => api.post('/bookings', bookingData),
  getAllBookings: () => api.get('/bookings'),
  getBookingById: (id) => api.get(`/bookings/${id}`),
  getBookingsByEmail: (email) => api.get(`/bookings/email/${email}`),
  cancelBooking: (id) => api.put(`/bookings/${id}/cancel`),
  getConfirmedBookings: () => api.get('/bookings/confirmed'),
  getCancelledBookings: () => api.get('/bookings/cancelled'),
};

export default api;

