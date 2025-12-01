import axios from 'axios';

const USER_API_URL = process.env.VUE_USER_API_URL || 'http://localhost:18080';

const apiClient = axios.create({
  baseURL: USER_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  login(credentials) {
    return apiClient.post('/users/login', credentials);
  }
};
