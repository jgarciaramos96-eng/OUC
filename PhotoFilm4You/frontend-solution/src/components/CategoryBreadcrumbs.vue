<!-- src/components/CategoryBreadcrumbs.vue -->
<template>
  <nav aria-label="breadcrumb" v-if="items && items.length">
    <ol class="breadcrumb">
      <li class="breadcrumb-item">
        <router-link :to="homeRoute">Inicio</router-link>
      </li>

      <li class="breadcrumb-item">
        <router-link to="/catalog">Catálogo</router-link>
      </li>

      <li
        v-for="(it, idx) in items"
        :key="it.id"
        :class="['breadcrumb-item', { active: idx === items.length - 1 }]"
        aria-current="page"
      >
        <template v-if="idx === items.length - 1">{{ it.name }}</template>
        <template v-else>
          <a @click.prevent="goTo(it)">{{ it.name }}</a>
        </template>
      </li>
    </ol>
  </nav>
</template>

<script>
export default {
  name: "CategoryBreadcrumbs",

  props: {
    items: { type: Array, default: () => [] }
  },

  computed: {
    homeRoute() {
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      const role = user.role;

      if (role === "admin") return "/adminHome";
      if (role === "user") return "/home";

      return "/";
    }
  },

  methods: {
    goTo(it) {
      this.$router.push(`/catalog/${it.id}`);
    }
  }
};
</script>

<style scoped>
.breadcrumb { background: transparent; padding-left: 0; margin-bottom: 1rem; }
.breadcrumb a { cursor: pointer; color: #0d6efd; text-decoration: none; }
</style>
