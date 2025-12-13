import axios from 'axios';

const USER_API_URL = process.env.VUE_USER_API_URL || 'http://localhost:18082';

const apiClient = axios.create({
  baseURL: USER_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  login(credentials) {
    return apiClient.post('/users/login', credentials);
  },
  createRentalRequest(payload) {
    return apiClient.post('/rentalRequests', payload);
  },
  getRentalRequests() {
    return apiClient.get('/rentalRequests');
  },
  getRentalRequest(id) {
    return apiClient.get(`/rentalRequests/${id}`);
  },
  getUser(id) {
    return apiClient.get(`/users/${id}`);
  },
};