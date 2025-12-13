import axios from 'axios';

const CATEGORY_API_URL = process.env.VUE_PRODUCT_API_URL || 'http://localhost:18081';

const apiClient = axios.create({
  baseURL: CATEGORY_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  getCategories() {
    return apiClient.get('/categories');
  },
  createCategory(payload) {
    return apiClient.post('/categories', payload);
  },
  updateCategory(payload) {
    return apiClient.put(`/categories/${payload.id}`, payload);
  },
  deleteCategory(categoryId) {
    return apiClient.delete(`/categories/${categoryId}`);
  },
  searchCategories(params = {}) {
    return apiClient.get('/categories/search', {params: params});
  }
};
