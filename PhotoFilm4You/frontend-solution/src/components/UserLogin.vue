<template>
  <div class="container mt-4">
    <div class="card shadow-sm">
      <div class="card-body">
        <h2 class="text-center mb-4">Login sencillo</h2>
        <form @submit.prevent="submitLogin">
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input
              v-model="email"
              class="form-control"
              type="email"
              required
            />
          </div>

          <div class="mb-3">
            <label class="form-label">Contraseña</label>
            <input
              v-model="password"
              class="form-control"
              type="password"
              required
            />
          </div>

          <button type="submit" class="btn btn-primary w-100">Entrar</button>
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
  import "bootstrap/dist/css/bootstrap.min.css";

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
        } catch (err) {
          console.error("Error en login", err);
          this.message = "Credenciales incorrectas. Revisa el email y la contraseña.";
          this.messageClass = "alert-danger";
        }
      },
    },
  };
</script>
