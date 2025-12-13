<template>
  <div class="container py-4">

    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item active" aria-current="page">
          Panel Admin
        </li>
      </ol>
    </nav>

    <h1 class="fw-bold mb-4 text-center">Panel Administrador</h1>

    <div class="row g-4 justify-content-center">
      <div class="col-md-4">
        <div class="admin-card" @click="$router.push('/admin/categories')">
          <i class="bi bi-diagram-3-fill card-icon"></i>
          <h4 class="mt-3">Gestionar Categorías</h4>
          <p class="text-muted">
            Crea, edita y organiza categorías del catálogo.
          </p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="admin-card" @click="$router.push('/product/add')">
          <i class="bi bi-box-seam-fill card-icon"></i>
          <h4 class="mt-3">Gestionar Productos</h4>
          <p class="text-muted">
            Añade nuevos productos y administra inventario.
          </p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="admin-card" @click="$router.push('/rental-requests')">
          <i class="bi bi-file-earmark-text-fill card-icon"></i>
          <h4 class="mt-3">Solicitudes de Alquiler</h4>
          <p class="text-muted">
            Revisa y gestiona todas las solicitudes.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminPanel",
  computed: {
    homeRoute() {
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      return user.role === "admin" ? "/adminHome" : "/home";
    }
  }
};
</script>

<style scoped>
.admin-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  cursor: pointer;
  transition: all 0.25s ease-in-out;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
}
.admin-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 18px rgba(0,0,0,0.15);
}
.card-icon {
  font-size: 50px;
  color: #0d6efd;
}
</style>
