<template>
  <div class="container py-4">

    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item active" aria-current="page">
          Panel Usuario
        </li>
      </ol>
    </nav>

    <h1 class="fw-bold mb-4 text-center">Bienvenido, {{ userName }}</h1>

    <div class="row g-4 justify-content-center">
      <div class="col-md-4">
        <div class="user-card" @click="$router.push('/catalog')">
          <i class="bi bi-collection-fill card-icon"></i>
          <h4 class="mt-3">Ver catálogo</h4>
          <p class="text-muted">
            Explora todos los productos disponibles para alquiler.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserPanel",
  computed: {
    userName() {
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      return user.fullName || user.email || "Usuario";
    }
  }
};
</script>

<style scoped>
.user-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  cursor: pointer;
  transition: all 0.25s ease-in-out;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
}
.user-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 18px rgba(0,0,0,0.15);
}
.card-icon {
  font-size: 50px;
  color: #0d6efd;
}
</style>
>>>>>>> 8345900 (Mis cambios locales antes de crear feature/catalog-united-view)
