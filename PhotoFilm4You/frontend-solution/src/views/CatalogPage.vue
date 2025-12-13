<template>
  <div class="container mt-5">
    <div class="mb-3">
      <button class="btn btn-secondary" @click="goBack">
        {{ userRole === 'admin' ? 'Volver al Panel Admin' : 'Volver al Inicio' }}
      </button>
    </div>

    <div class="container mt-4">
      <h2 class="mb-4">Catálogo</h2>

      <div v-if="loading" class="text-center py-5">Cargando categorías...</div>
      <div v-if="error" class="alert alert-danger">{{ error }}</div>

      <div class="row g-4">
        <div
          v-for="cat in rootCategories"
          :key="cat.id"
          class="col-12 col-sm-6 col-md-4 col-lg-3"
        >
          <div class="card h-100 category-card" @click="openCategory(cat.id)">
            <div class="card-body d-flex flex-column justify-content-center align-items-center">
              <div class="cat-initial mb-3">{{ cat.name.charAt(0).toUpperCase() }}</div>
              <h5 class="card-title text-center">{{ cat.name }}</h5>
              <p class="text-muted small text-center mt-2">{{ cat.description }}</p>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import categoryApi from "@/services/category/api";

export default {
  name: "CatalogPage",

  data() {
    return {
      categories: [],
      loading: false,
      error: null
    };
  },

  computed: {
    rootCategories() {
      return this.categories.filter(c => c.parentId === null || c.parentId === undefined);
    },

    userRole() {
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      return user.role || null;
    }
  },

  methods: {
    async loadCategories() {
      try {
        this.loading = true;
        const res = await categoryApi.getCategories();
        this.categories = res.data;
      } catch (e) {
        console.error(e);
        this.error = "No se pudieron cargar las categorías.";
      } finally {
        this.loading = false;
      }
    },

    openCategory(id) {
      this.$router.push(`/catalog/${id}`);
    },

    goBack() {
      if (this.userRole === "admin") {
        this.$router.push("/adminHome");
      } else if (this.userRole === "user") {
        this.$router.push("/home");
      } else {
        this.$router.push("/");
      }
    }
  },

  mounted() {
    this.loadCategories();
  }
};
</script>

<style scoped>
.category-card { cursor: pointer; transition: transform .12s ease; }
.category-card:hover { transform: translateY(-6px); box-shadow: 0 10px 30px rgba(0,0,0,.08); }
.cat-initial {
  width: 72px; height: 72px; border-radius: 50%; display:flex; align-items:center; justify-content:center;
  background: linear-gradient(135deg,#0d6efd,#6610f2); color:white; font-size:28px; font-weight:700;
}
</style>
