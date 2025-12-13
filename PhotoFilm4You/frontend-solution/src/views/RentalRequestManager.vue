<template>
  <div class="container mt-5">
    <div class="mb-3">
      <button class="btn btn-secondary" @click="$router.push('/adminHome')">Volver al Panel Admin</button>
    </div>
    <div class="card shadow-sm">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <h5 class="mb-0">Solicitudes de Alquiler</h5>
          <button class="btn btn-outline-secondary btn-sm" @click="loadRentalRequests">Refrescar</button>
        </div>

        <div v-if="rentalRequests.length === 0" class="text-muted">No hay solicitudes de alquiler.</div>

        <table v-else class="table table-sm table-striped align-middle mb-0">
          <thead>
          <tr>
            <th>ID</th>
            <th>Fecha Inicio</th>
            <th>Fecha Fin</th>
            <th>ID Usuario</th>
            <th>Productos</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="request in rentalRequests" :key="request.id">
            <td>{{ request.id }}</td>
            <td>{{ request.startDate }}</td>
            <td>{{ request.endDate }}</td>
            <td>{{ request.userId }}</td>
            <td>{{ request.products ? request.products.length : 0 }}</td>
            <td class="text-end">
              <button class="btn btn-outline-primary btn-sm" @click="$router.push(`/rental-request/${request.id}`)">Ver Detalles</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import userApi from '@/services/user/api';

export default {
  name: 'RentalRequestManager',
  data() {
    return {
      rentalRequests: [],
      errorMessage: '',
    };
  },
  mounted() {
    this.loadRentalRequests();
  },
  methods: {
    async loadRentalRequests() {
      try {
        const res = await userApi.getRentalRequests();
        this.rentalRequests = res.data;
        this.errorMessage = '';
      } catch (err) {
        console.error('Error cargando solicitudes de alquiler', err);
        this.errorMessage = 'No se pudieron cargar las solicitudes de alquiler.';
      }
    },
  },
};
</script>
