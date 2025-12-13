<!-- src/views/CategoryView.vue -->
<template>
  <div class="container mt-4">
    <CategoryBreadcrumbs :items="breadcrumb" />

    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h2 class="mb-0">{{ category?.name || 'Categoría' }}</h2>
        <p class="text-muted small mb-0">{{ category?.description }}</p>
      </div>
    </div>

    <div v-if="loading" class="text-center py-5">Cargando...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <div v-if="subcategories.length">
      <h5 class="mb-3">Subcategorías</h5>
      <div class="row g-4 mb-4">
        <div v-for="sub in subcategories" :key="sub.id" class="col-12 col-sm-6 col-md-4 col-lg-3">
          <div class="card category-card h-100" @click="openCategory(sub.id)">
            <div class="card-body text-center">
              <div class="cat-initial mb-2">{{ sub.name.charAt(0).toUpperCase() }}</div>
              <h6 class="mb-0">{{ sub.name }}</h6>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="!subcategories.length">
      <h5 class="mb-3">Productos</h5>
      <div v-if="products.length === 0" class="text-muted">No hay productos en esta categoría.</div>
      <div class="row g-4">
        <div v-for="p in products" :key="p.id" class="col-12 col-sm-6 col-md-4 col-lg-3">
          <ProductCard :product="p" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import categoryApi from "@/services/category/api";
import productApi from "@/services/product/api";
import ProductCard from "@/components/ProductCard.vue";
import CategoryBreadcrumbs from "@/components/CategoryBreadcrumbs.vue";

export default {
  name: "CategoryView",
  components: { ProductCard, CategoryBreadcrumbs },

  data() {
    return {
      category: null,
      categories: [],
      subcategories: [],
      products: [],
      loading: false,
      error: null,
      breadcrumb: []
    };
  },

  watch: {
    '$route.params.id': {
      immediate: true,
      handler() {
        this.load();
      }
    }
  },

  methods: {
    async load() {
      const id = Number(this.$route.params.id);
      if (!id) {
        this.error = "Categoría inválida";
        return;
      }

      try {
        this.loading = true;
        const resCat = await categoryApi.getCategories();
        this.categories = resCat.data;

        this.category = this.categories.find(c => c.id === id) || null;

        this.subcategories = this.categories.filter(c => c.parentId === id);

        if (!this.subcategories.length) {
          const resProd = await productApi.searchProducts({categoryId: id});
          this.products = resProd.data || [];
        } else {
          this.products = [];
        }

        this.buildBreadcrumb(id);
      } catch (e) {
        console.error(e);
        this.error = "Error cargando categoría o productos.";
      } finally {
        this.loading = false;
      }
    },

    buildBreadcrumb(id) {
      const trail = [];
      let cur = this.categories.find(c => c.id === id);
      while (cur) {
        trail.unshift({ id: cur.id, name: cur.name });
        if (!cur.parentId) break;
        cur = this.categories.find(c => c.id === cur.parentId);
      }
      this.breadcrumb = trail;
    },

    openCategory(id) {
      this.$router.push(`/catalog/${id}`);
    }
  }
};
</script>

<style scoped>
.category-card { cursor: pointer; transition: transform .12s ease; }
.category-card:hover { transform: translateY(-6px); box-shadow: 0 10px 30px rgba(0,0,0,.08); }
.cat-initial {
  width: 56px; height: 56px; border-radius: 50%; display:flex; align-items:center; justify-content:center;
  background: linear-gradient(135deg,#ffc107,#fd7e14); color:black; font-size:20px; font-weight:700;
}
</style>
