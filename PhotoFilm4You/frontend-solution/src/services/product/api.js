import axios from 'axios';

const PRODUCT_API_URL = process.env.VUE_APP_PRODUCT_API_URL || 'http://localhost:18081';

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
  getProductById(id) {
    return apiClient.get(`/products/${id}`);
  },
  createProduct(product) {
    return apiClient.post('/products', product);
  },
  updateProduct(product) {
    return apiClient.put(`/products/${product.id}`, product);
  },
  deleteProduct(productId) {
    return apiClient.delete(`/products/${productId}`);
  },
  searchProducts(params = {}) {
    return apiClient.get('products/search?', {params: params});
  }
};
