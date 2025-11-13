import axios from 'axios';

const PRODUCT_API_URL = process.env.VUE_PRODUCT_API_URL || 'http://localhost:18081';

const apiClient = axios.create({
  baseURL: PRODUCT_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  getProducts() {
    return apiClient.get('/products');
  },
  createProduct(product) {
    return apiClient.post('/products', product);
  },
  getCategories() {
    return apiClient.get('/categories');
  }
};