<template>
  <div class="container mt-5">
    <div class="mb-3">
      <button class="btn btn-secondary" @click="$router.push('/adminHome')">Volver al Panel Admin</button>
    </div>
    <div class="card shadow-sm">
      <div class="card-body">
        <h2 class="text-center mb-3">{{ isEditing ? 'Editar categoría' : 'Crear categoría' }}</h2>
        <form @submit.prevent="submitForm" class="row g-3">
          <div class="col-md-4">
            <label class="form-label">Nombre</label>
            <input v-model="form.name" class="form-control" required />
          </div>
          <div class="col-md-4">
            <label class="form-label">Descripción</label>
            <input v-model="form.description" class="form-control" required />
          </div>
          <div class="col-md-4">
            <label class="form-label">Padre</label>
            <select v-model.number="form.parentId" class="form-select">
              <option :value="null">Sin padre</option>
              <option
                  v-for="cat in categories"
                  :key="cat.id"
                  :value="cat.id"
                  :disabled="cat.id === form.id"
              >
                {{ cat.name }}
              </option>
            </select>
          </div>
          <div class="col-12">
            <button type="submit" class="btn btn-primary">
              {{ isEditing ? 'Actualizar' : 'Crear' }}
            </button>
            <button v-if="isEditing" type="button" class="btn btn-link" @click="resetForm">
              Cancelar
            </button>
          </div>
        </form>

        <div v-if="message" class="alert alert-info mt-3 mb-0">
          {{ message }}
        </div>
        <div v-if="errorMessage" class="alert alert-danger mt-3 mb-0">
          {{ errorMessage }}
        </div>
      </div>

      <div class="card-body border-top">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <h5 class="mb-0">Categorías</h5>
          <button class="btn btn-outline-secondary btn-sm" @click="loadCategories">Refrescar</button>
        </div>

        <div v-if="categories.length === 0" class="text-muted">No hay categorías.</div>

        <table v-else class="table table-sm table-striped align-middle mb-0">
          <thead>
          <tr>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Padre</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="cat in categories" :key="cat.id">
            <td>{{ cat.name }}</td>
            <td>{{ cat.description }}</td>
            <td>{{ parentName(cat.parentId) }}</td>
            <td class="text-end">
              <button class="btn btn-outline-primary btn-sm me-2" @click="startEdit(cat)">
                Editar
              </button>
              <button class="btn btn-danger btn-sm" @click="removeCategory(cat.id)">
                Eliminar
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import categoryApi from '@/services/category/api';

export default {
  name: 'CategoryManager',
  data() {
    return {
      categories: [],
      form: {
        id: null,
        name: '',
        description: '',
        parentId: null,
      },
      isEditing: false,
      message: '',
      errorMessage: '',
    };
  },
  mounted() {
    this.loadCategories();
  },
  methods: {
    async loadCategories() {
      try {
        const res = await categoryApi.getCategories();
        this.categories = res.data;
      } catch (err) {
        console.error('Error cargando categorías', err);
        this.errorMessage = 'No se pudieron cargar las categorías.';
      }
    },
    async submitForm() {
      this.message = '';
      this.errorMessage = '';

      const parentIdValue = this.form.parentId == null ? null : Number(this.form.parentId);
      const basePayload = {
        name: this.form.name.trim(),
        description: this.form.description.trim(),
        parentCategoryId: parentIdValue,
      };

      if (parentIdValue != null) {
        basePayload.parent = { id: parentIdValue };
      }

      if (!basePayload.name || !basePayload.description) {
        this.errorMessage = 'Nombre y descripción son obligatorios.';
        return;
      }

      try {
        if (this.isEditing && this.form.id != null) {
          await categoryApi.updateCategory({ ...basePayload, id: this.form.id });
          this.message = 'Categoría actualizada.';
        } else {
          await categoryApi.createCategory(basePayload);
          this.message = 'Categoría creada.';
        }
        await this.loadCategories();
        this.resetForm();
      } catch (err) {
        console.error('Error guardando categoría', err);
        this.errorMessage = 'No se pudo guardar la categoría.';
      } finally {
        setTimeout(() => {
          this.message = '';
          this.errorMessage = '';
        }, 4000);
      }
    },
    startEdit(cat) {
      this.form = {
        id: cat.id,
        name: cat.name,
        description: cat.description,
        parentId: cat.parentId ?? null,
      };
      this.isEditing = true;
    },
    async removeCategory(id) {
      const confirmed = window.confirm('¿Eliminar esta categoría?');
      if (!confirmed) return;

      try {
        await categoryApi.deleteCategory(id);
        this.categories = this.categories.filter((c) => c.id !== id);
        this.message = 'Categoría eliminada.';
      } catch (err) {
        console.error('Error eliminando categoría', err);
        this.errorMessage = 'No se pudo eliminar la categoría.';
      } finally {
        setTimeout(() => {
          this.message = '';
          this.errorMessage = '';
        }, 4000);
      }
    },
    parentName(parentId) {
      if (parentId === null || parentId === undefined || parentId === '') return '—';
      const pid = Number(parentId);
      const parent = this.categories.find((c) => c.id === pid);
      return parent ? parent.name : parentId;
    },
    resetForm() {
      this.form = {
        id: null,
        name: '',
        description: '',
        parentId: null,
      };
      this.isEditing = false;
    },
  },
};
</script>
