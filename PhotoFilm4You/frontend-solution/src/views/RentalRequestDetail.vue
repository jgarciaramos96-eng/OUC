<template>
  <div class="container mt-5">
    <div class="card shadow-sm">
      <div class="card-body">
        <h2 class="mb-4">Detalles de Solicitud de Alquiler</h2>

        <div v-if="rentalRequest">
          <div class="row mb-3">
            <div class="col-md-3">
              <strong>ID:</strong> {{ rentalRequest.id }}
            </div>
            <div class="col-md-3">
              <strong>Fecha Inicio:</strong> {{ rentalRequest.startDate }}
            </div>
            <div class="col-md-3">
              <strong>Fecha Fin:</strong> {{ rentalRequest.endDate }}
            </div>
          </div>

          <h5>Usuario</h5>
          <div v-if="user" class="row mb-4">
            <div class="col-md-3">
              <strong>ID Usuario:</strong> {{ rentalRequest.userId }}
            </div>
            <div class="col-md-3">
              <strong>Nombre:</strong> {{ user.fullName }}
            </div>
            <div class="col-md-3">
              <strong>Email:</strong> {{ user.email }}
            </div>
            <div class="col-md-3">
              <strong>Teléfono:</strong> {{ user.phoneNumber }}
            </div>
          </div>

          <h5>Productos</h5>
          <table class="table table-sm table-striped">
            <thead>
            <tr>
              <th>ID Producto</th>
              <th>Nombre</th>
              <th>Unidades</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="product in rentalRequest.products" :key="product.productId">
              <td>{{ product.productId }}</td>
              <td>{{ product.name }}</td>
              <td>{{ product.units }}</td>
            </tr>
            </tbody>
          </table>
        </div>

        <div v-else-if="loading" class="text-center">
          Cargando...
        </div>

        <div v-else class="alert alert-danger">
          Error al cargar la solicitud de alquiler.
        </div>

        <div class="mt-4">
          <button class="btn btn-secondary" @click="$router.push('/rental-requests')">Volver</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import userApi from '@/services/user/api';
import productApi from '@/services/product/api';

export default {
  name: 'RentalRequestDetail',
  data() {
    return {
      rentalRequest: null,
      user: null,
      loading: true,
      error: false,
    };
  },
  mounted() {
    this.loadRentalRequest();
  },
  methods: {
    async loadRentalRequest() {
      try {
        const id = this.$route.params.id;
        const res = await userApi.getRentalRequest(id);
        this.rentalRequest = res.data;

        if (this.rentalRequest.products && this.rentalRequest.products.length > 0) {
          this.rentalRequest.products = await Promise.all(
            this.rentalRequest.products.map(async (prod) => {
              try {
                const res = await productApi.getProductById(prod.productId);
                return { ...prod, name: res.data.name };
              } catch (err) {
                console.error('Error fetching product', prod.productId, err);
                return { ...prod, name: 'Desconocido' };
              }
            })
          );
        }

        this.user = (await userApi.getUser(this.rentalRequest.userId)).data;

        this.loading = false;
      } catch (err) {
        console.error('Error cargando solicitud de alquiler', err);
        this.error = true;
        this.loading = false;
      }
    },
  },
};
</script>
