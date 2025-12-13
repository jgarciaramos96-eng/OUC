<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4">

      <div class="navbar-brand d-flex align-items-center" @click="goHome">
        <img :src="logo" height="40" class="me-2" />
        <span class="fw-bold fs-4 text-white">Photo&Film4You</span>
      </div>

      <div class="ms-auto d-flex align-items-center gap-4">

        <template v-if="!isLoggedIn">
          <button class="btn btn-outline-light btn-sm" @click="goLogin">Login</button>
        </template>

        <template v-else>
          <button class="btn btn-outline-info btn-sm" @click="goCatalog">
            Ver catálogo
          </button>

          <div class="dropdown">
            <div class="d-flex align-items-center text-white"
                 style="cursor: pointer;"
                 data-bs-toggle="dropdown">
              <i class="bi bi-person-circle fs-4 me-2"></i>
              <span>{{ userName }}</span>
            </div>

            <ul class="dropdown-menu dropdown-menu-end">
              <li><a class="dropdown-item text-danger" @click="logout">Cerrar sesión</a></li>
            </ul>
          </div>
        </template>

      </div>
    </nav>

    <router-view />
  </div>
</template>

<script>
import { userStore } from "@/store/userStore.js";
import logo from "@/assets/logoHome.png";

export default {
  name: "App",

  data() {
    return {
      logo,
      store: userStore
    };
  },

  computed: {
    isLoggedIn() {
      return !!this.store.user;
    },
    userName() {
      return this.store.user?.email || this.store.user?.name || "Usuario";
    }
  },

  methods: {
    goHome() {
      if (this.isLoggedIn) this.$router.push("/home");
      else this.$router.push("/");
    },
    goLogin() {
      this.$router.push("/login");
    },
    goCatalog() {
      this.$router.push("/catalog");
    },
    logout() {
      this.store.logout();
      this.$router.push("/");
    }
  }
};
</script>
