<template>
  <div class="container mt-5">
    <div class="mb-3">
      <button class="btn btn-secondary" @click="$router.push('/adminHome')">Volver al Panel Admin</button>
    </div>
    <div class="card shadow-sm">
      <div class="card-body">
        <h2 class="text-center mb-4">
          {{ isEditing ? 'Editar Producto' : 'Alta de Producto' }}
        </h2>

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
            {{ isEditing ? 'Actualizar' : 'Guardar' }}
          </button>
        </form>

        <div v-if="message" class="alert alert-info mt-4 text-center">
          {{ message }}
        </div>

        <div class="mt-4">
          <div class="card-body">
            <h2 class="text-center mb-0">Productos</h2>
            <button class="btn btn-outline-secondary btn-sm" @click="loadProducts">
              Refrescar
            </button>
          </div>

          <div v-if="products.length === 0" class="text-muted">
            No hay productos.
          </div>

          <table v-else class="table table-sm table-striped align-middle">
            <thead>
            <tr>
              <th>Nombre</th>
              <th>Marca</th>
              <th>Modelo</th>
              <th>Precio diario</th>
              <th>Categoría</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="prod in products" :key="prod.id">
              <td>{{ prod.name }}</td>
              <td>{{ prod.brand }}</td>
              <td>{{ prod.model }}</td>
              <td>{{ prod.dailyPrice }}</td>
              <td>{{ prod.categoryId }}</td>
              <td class="text-end">
                <button class="btn btn-outline-primary btn-sm me-2" @click="startEdit(prod)">
                  Editar
                </button>
                <button class="btn btn-danger btn-sm" @click="removeProduct(prod.id)">
                  Eliminar
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import productApi from '@/services/product/api';
import categoryApi from '@/services/category/api';
import "bootstrap/dist/css/bootstrap.min.css";

export default {
  name: "ProductForm",
  data() {
    return {
      product: {
        id: null,
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
      products: [],
      isEditing: false,
    };
  },
  mounted() {
    this.loadCategories();
    this.loadProducts();
  },
  methods: {
    async loadCategories() {
      try {
        const res = await categoryApi.getCategories();
        this.categories = res.data;
        this.buildRootCategories();
      } catch (err) {
        console.error("Error cargando categorías", err);
      }
    },

    async submitForm() {
      try {
        if (this.isEditing && this.product.id != null) {
          await productApi.updateProduct(this.product);
          this.message = "Producto actualizado correctamente.";
        } else {
          await productApi.createProduct(this.product);
          this.message = "Producto creado correctamente.";
        }
        this.resetForm();
        await this.loadProducts();
      } catch (err) {
        console.error("Error guardando producto", err);
        this.message = "Error al guardar el producto.";
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

    async loadProducts() {
      try {
        const res = await productApi.getProducts();
        this.products = res.data;
      } catch (err) {
        console.error("Error cargando productos", err);
        this.message = "No se pudieron cargar los productos.";
      }
    },

    async removeProduct(id) {
      const confirmed = window.confirm("¿Eliminar este producto?");
      if (!confirmed) return;

      try {
        await productApi.deleteProduct(id);
        this.products = this.products.filter((p) => p.id !== id);
        this.message = "Producto eliminado.";
      } catch (err) {
        console.error("Error eliminando producto", err);
        this.message = "No se pudo eliminar el producto.";
      } finally {
        setTimeout(() => {
          this.message = "";
        }, 3000);
      }
    },

    startEdit(prod) {
      this.product = { ...prod };
      this.isEditing = true;
      this.setCategoryPath(prod.categoryId);
    },

    setCategoryPath(categoryId) {
      if (!categoryId) {
        this.buildRootCategories();
        return;
      }

      const path = [];
      let current = this.categories.find((c) => c.id === categoryId);
      while (current) {
        path.unshift(current.id);
        current = this.categories.find((c) => c.id === current.parentId);
      }

      const roots = this.categories.filter((c) => c.parentId === null);
      const levels = [roots];
      path.forEach((catId) => {
        const subs = this.categories.filter((c) => c.parentId === catId);
        if (subs.length > 0) {
          levels.push(subs);
        }
      });

      this.categoryLevels = levels;
      this.selectedCategories = path;
      this.product.categoryId = categoryId;
    },

    resetForm() {
      this.product = {
        id: null,
        name: "",
        brand: "",
        model: "",
        description: "",
        dailyPrice: "",
        categoryId: "",
      };
      this.isEditing = false;

      if (this.categories.length > 0) {
        this.buildRootCategories();
      } else {
        this.selectedCategories = [""];
        this.categoryLevels = [];
      }
    },

  },
};
</script>
