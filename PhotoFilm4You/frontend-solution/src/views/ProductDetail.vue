<template>
  <div class="container mt-5">
    <div class="mb-3">
      <button class="btn btn-secondary" @click="$router.push('/catalog')">Volver</button>
    </div>
    <div class="card shadow-sm">
      <div class="card-body">
        <!-- Loading state -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Cargando...</span>
          </div>
          <p class="mt-3">Cargando producto...</p>
        </div>

        <!-- Error state -->
        <div v-else-if="error" class="alert alert-danger text-center">
          <p>{{ error }}</p>
        </div>

        <!-- Product content -->
        <div v-else-if="product">
          <div class="row">
            <!-- Product Image -->
            <div class="col-md-6 mb-4">
              <img
                src="https://picsum.photos/1024/750"
                :alt="product.name"
                class="img-fluid rounded"
              />
            </div>

            <!-- Product Info -->
            <div class="col-md-6">
              <h2 class="mb-3">{{ product.name }}</h2>

              <div class="mb-3">
                <span class="badge bg-secondary me-2">{{ product.brand }}</span>
                <span class="text-muted">Modelo: {{ product.model }}</span>
              </div>

              <p class="text-muted">{{ product.description }}</p>

              <hr>

              <div class="mb-4">
                <h5 class="text-primary">{{ formatPrice(product.dailyPrice) }} <small class="text-muted">/ día</small></h5>
              </div>

              <!-- Date Selection -->
              <div class="mb-3">
                <label class="form-label">Fecha de inicio</label>
                <input
                  type="date"
                  v-model="startDate"
                  class="form-control"
                  :min="today"
                  @change="validateDates"
                />
              </div>

              <div class="mb-3">
                <label class="form-label">Fecha de fin</label>
                <input
                  type="date"
                  v-model="endDate"
                  class="form-control"
                  :min="startDate || today"
                  @change="validateDates"
                />
              </div>

              <div class="mb-3">
                <label class="form-label">Unidades</label>
                <input
                  type="number"
                  v-model.number="units"
                  class="form-control"
                  min="1"
                  step="1"
                />
              </div>

              <!-- Price Summary -->
              <div v-if="totalDays > 0" class="alert alert-info">
                <div class="d-flex justify-content-between align-items-center">
                  <div>
                    <div>{{ totalDays }} día{{ totalDays > 1 ? 's' : '' }} de alquiler</div>
                    <small class="text-muted">{{ units }} unidad{{ units > 1 ? 'es' : '' }}</small>
                  </div>
                  <strong class="fs-5">{{ formatPrice(totalPrice) }}</strong>
                </div>
              </div>

              <!-- Add to Cart Button -->
              <button
                class="btn btn-primary w-100 mb-3"
                @click="addToCart"
                :disabled="!canAddToCart"
              >
                Añadir al carrito
              </button>

              <small class="text-muted d-block text-center">
                Selecciona las fechas para calcular el precio total
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Toast Notification -->
    <div v-if="showToast" class="toast-container position-fixed bottom-0 end-0 p-3">
      <div class="toast show" role="alert">
        <div class="toast-header bg-success text-white">
          <strong class="me-auto">✓ Producto añadido</strong>
          <button type="button" class="btn-close btn-close-white" @click="showToast = false"></button>
        </div>
        <div class="toast-body">
          {{ product.name }} se ha añadido al carrito correctamente
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/services/product/api';
import userApi from '@/services/user/api';
import "bootstrap/dist/css/bootstrap.min.css";
import { jwtDecode } from 'jwt-decode';

export default {
  name: 'ProductDetail',

  data() {
    return {
      product: null,
      loading: false,
      error: null,
      startDate: '',
      endDate: '',
      units: 1,
      showToast: false,
      today: new Date().toISOString().split('T')[0]
    };
  },

  computed: {
    totalDays() {
      if (!this.startDate || !this.endDate) return 0;

      const start = new Date(this.startDate);
      const end = new Date(this.endDate);
      const diffTime = end - start;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

      return diffDays > 0 ? diffDays : 0;
    },

    totalPrice() {
      if (!this.product || this.totalDays === 0) return 0;
      return this.product.dailyPrice * this.totalDays * this.units;
    },

    canAddToCart() {
      return this.startDate && this.endDate && this.totalDays > 0 && this.units > 0;
    }
  },

  created() {
    this.loadProduct();
  },

  watch: {
    '$route.params.id': 'loadProduct'
  },

  methods: {
    async loadProduct() {
      const productId = this.$route.params.id;

      if (!productId) {
        this.error = 'ID de producto no válido';
        return;
      }

      this.loading = true;
      this.error = null;

      try {
        const response = await api.getProductById(productId);
        this.product = response.data;
      } catch (err) {
        console.error('Error al cargar el producto:', err);
        if (err.response?.status === 404) {
          this.error = 'Producto no encontrado';
        } else {
          this.error = 'Error al cargar el producto. Por favor, intenta de nuevo.';
        }
      } finally {
        this.loading = false;
      }
    },

    validateDates() {
      if (this.startDate && this.endDate) {
        const start = new Date(this.startDate);
        const end = new Date(this.endDate);

        if (end < start) {
          this.endDate = this.startDate;
        }
      }
    },

    formatPrice(price) {
      return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR'
      }).format(price);
    },

    async addToCart() {
      if (!this.canAddToCart) return;

      const token = localStorage.getItem("token");
      const userId = jwtDecode(token).userId;
      const rentalRequest = {
        userId: userId,
        startDate: this.startDate,
        endDate: this.endDate,
        products: [
          {
            productId: this.product.id,
            units: this.units
          }
        ]
      };

      try {
        await userApi.createRentalRequest(rentalRequest);

        this.showToast = true;

        setTimeout(() => {
          this.showToast = false;
        }, 3000);
      } catch (err) {
        alert('Error al añadir el producto al carrito. Por favor, intenta de nuevo.');
      }
    }
  }
};
</script>

<style scoped>
.toast-container {
  z-index: 1050;
}

.toast {
  min-width: 300px;
}

.spinner-border {
  width: 3rem;
  height: 3rem;
}
</style>