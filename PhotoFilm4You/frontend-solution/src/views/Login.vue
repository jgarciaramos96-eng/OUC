<template>
  <div class="container mt-4 d-flex justify-content-center">
    <div class="card shadow-sm w-50">
      <div class="card-body">
        <h2 class="text-center mb-4">Login</h2>

        <form @submit.prevent="submitLogin">
          <div class="container-sm d-flex justify-content-center w-30">
            <div class="mb-3 w-30">
              <label class="form-label">Email</label>
              <input
                v-model="email"
                class="form-control"
                type="email"
                required
              />
            </div>
          </div>

          <div class="container-sm d-flex justify-content-center w-30">
            <div class="mb-3 w-30">
              <label class="form-label">Contrasena</label>
              <input
                v-model="password"
                class="form-control"
                type="password"
                required
              />
            </div>
          </div>

          <div class="text-center">
            <button type="submit" class="btn btn-primary w-20">Entrar</button>
          </div>
        </form>

        <div v-if="message" class="alert mt-4" :class="messageClass">
          {{ message }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/services/user/api';
import { jwtDecode } from "jwt-decode";
import "bootstrap/dist/css/bootstrap.min.css";
import { userStore } from "@/store/userStore.js";

export default {
  name: "UserLogin",
  data() {
    return {
      email: "",
      password: "",
      message: "",
      messageClass: "alert-info",
    };
  },
  methods: {
    async submitLogin() {
      this.message = "";
      try {
        const response = await api.login({ email: this.email, password: this.password });

        this.message = `Bienvenido, ${response.data.fullName}`;
        this.messageClass = "alert-success";

        localStorage.setItem("token", response.data.token);

        const payload = jwtDecode(response.data.token);

        userStore.setUser({
          email: payload.sub,
          fullName: response.data.fullName,
          role: payload.role
        });

        const redirectQuery = this.$route.query.redirect;
        const redirectPath = Array.isArray(redirectQuery) ? redirectQuery[0] : redirectQuery;
        const fallback = payload.role === 'admin' ? '/adminHome' : '/home';

        setTimeout(() => {
          this.$router.push(redirectPath || fallback);
        }, 1000);

      } catch (err) {
        console.error("Error en login", err);
        this.message = "Credenciales incorrectas. Revisa el email y la contraseña.";
        this.messageClass = "alert-danger";
      }
    }
  },
};
</script>
