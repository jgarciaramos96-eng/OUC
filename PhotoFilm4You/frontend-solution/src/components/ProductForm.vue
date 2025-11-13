<template>
  <div class="container mt-5">
    <div class="card shadow-sm">
      <div class="card-body">
        <h2 class="text-center mb-4">Alta de Producto</h2>

        <form @submit.prevent="submitForm" class="needs-validation" novalidate>
          <div class="mb-3">
            <label class="form-label">Nombre</label>
            <input v-model="product.name" class="form-control" required />
          </div>

          <div class="mb-3">
            <label class="form-label">Marca</label>
            <input v-model="product.brand" class="form-control" required />
          </div>

          <div class="mb-3">
            <label class="form-label">Modelo</label>
            <input v-model="product.model" class="form-control" required />
          </div>

          <div class="mb-3">
            <label class="form-label">Descripción</label>
            <input v-model="product.description" class="form-control" required />
          </div>

          <div class="mb-3">
            <label class="form-label">Precio diario</label>
            <input
              type="number"
              v-model.number="product.dailyPrice"
              class="form-control"
              required
            />
          </div>

          <div v-for="(level, index) in categoryLevels" :key="index" class="mb-3">
            <label class="form-label" v-if="index === 0">
              Categoría
            </label>
            <select class="form-select" v-model.number="selectedCategories[index]" @change="onCategoryChange(index)" required>
              <option disabled value="">Selecciona una opción</option>
              <option v-for="cat in level" :key="cat.id" :value="cat.id">
                {{ cat.name }}
              </option>
            </select>
          </div>

          <button type="submit" class="btn btn-primary w-100">
            Guardar
          </button>
        </form>

        <div v-if="message" class="alert alert-info mt-4 text-center">
          {{ message }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import api from '@/services/product/api';
  import "bootstrap/dist/css/bootstrap.min.css";

  export default {
    name: "ProductForm",
    data() {
      return {
        product: {
          name: "",
          brand: "",
          model: "",
          description: "",
          dailyPrice: "",
          categoryId: "",
        },
        categories: [],
        categoryLevels: [],
        selectedCategories: [],
        message: "",
      };
    },
    mounted() {
      this.loadCategories();
    },
    methods: {
      async loadCategories() {
        try {
          const res = await api.getCategories();
          this.categories = res.data;
          this.buildRootCategories();
        } catch (err) {
          console.error("Error cargando categorías", err);
        }
      },

      async submitForm() {
        try {
          await api.createProduct(this.product);
          this.message = "Producto creado correctamente.";
          this.resetForm();
        } catch (err) {
          console.error("Error creando producto", err);
          this.message = "Error al crear el producto.";
        } finally {
          setTimeout(() => {
            this.message = "";
          }, 5000);
        }
      },

      buildRootCategories() {
        const roots = this.categories.filter((c) => c.parentId === null);
        this.categoryLevels = [roots];
        this.selectedCategories = [""];
      },

      onCategoryChange(index) {
        const selectedId = this.selectedCategories[index];
        this.categoryLevels.splice(index + 1);
        this.selectedCategories.splice(index + 1);
        const subs = this.categories.filter((c) => c.parentId === selectedId);
        if (subs.length > 0) {
          this.categoryLevels.push(subs);
          this.selectedCategories.push("");
        } else {
          this.product.categoryId = selectedId;
        }
      },

      resetForm() {
        this.product = {
          name: "",
          brand: "",
          model: "",
          description: "",
          dailyPrice: "",
          categoryId: "",
        };
      },

    },
  };
</script>
